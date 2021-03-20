import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 
import {SHOP_LOAD_COUNT} from '../utils/constants'

class Search extends React.Component{

    constructor(props){
        super(props);
        this.loadCount=0;
        this.searchText="";
        this.selectedCategories=[];
        this.sort="default";

        this.sorts=[
            {value:"default",name:"Default"},
            {value:"newness",name:"Newest first"},
            {value:"timeleft",name:"Time left"},
            {value:"priceAsc",name:"By Price Ascending"},
            {value:"priceDesc",name:"By Price Descending"}
        ]

        this.state={
            items:[],
            loadMore: true,
            categories:[],
            selectedCategories:[],
            display:"grid"
        }
    }
        
    componentDidMount=()=>{
        this.loadCategories();
        this.context.setSearchCallback((text)=>{this.textChanged(text);});
        this.context.search(null);
    }

    componentWillUnmount=()=>{
        this.context.removeSearchCallback();
    }

    textChanged=(text)=>{
        this.setState({['loadMore']:true});
        this.loadCount=0;
        this.searchText=text;
        this.load()
    }

    sortChanged=(event)=>{
        if(this.sort!=event.target.value){
            this.setState({['loadMore']:true});
            this.loadCount=0;
            this.sort=event.target.value;
            this.load()
        }
    }

    displayChanged=(event)=>{
        this.setState({['display']:event})
    }
    

    load=()=>{
        searchItems(this.searchText,this.selectedCategories,this.loadCount,SHOP_LOAD_COUNT,this.sort,(success, data)=>{
            if(success){
                if(data.length<SHOP_LOAD_COUNT){
                    this.setState({['loadMore']:false});
                }else if(data.length==SHOP_LOAD_COUNT){
                    searchItems(this.searchText,this.selectedCategories,this.loadCount+1,SHOP_LOAD_COUNT,this.sort,(success, data)=>{
                        if(success)
                            if(data.length==0){
                                this.setState({['loadMore']:false});
                            }
                    }); 
                }
                if(this.loadCount!=0){
                    this.setState({['items']:[...this.state.items,...data]});
                }else{
                    this.setState({['items']:data});
                }
                this.loadCount=this.loadCount+1;
            }
        })
    }

    loadCategories=()=>{
        getAllCategories((success,data)=>{
            if(success){
                this.setState({['categories']: data.slice(1)})
            }else{
                console.log("Cannot fetch categories")
            }
        });
    }

    selectCategory=(cat)=>{
        if(this.state.selectedCategories.includes(cat)){
            this.setState({['selectedCategories']:this.state.selectedCategories.filter((x)=>x!=cat)});
            this.selectedCategories=this.selectedCategories.filter((x)=>x!=cat);
        }else{
            this.setState({['selectedCategories']:[...this.state.selectedCategories,cat]})
            this.selectedCategories.push(cat);
        }

        this.loadCount=0;
        this.setState({['loadMore']:true});
        this.load()
    }

    render(){
        return(
        <div className="shopContainer">
           
            <div className="categoriesList">
                <div className="categoryButtonTitle">PRODUCT CATEGORIES</div>
                {this.state.categories.map(category=>
                <div className={this.state.selectedCategories.includes(category.id)?"categoryButtonSelected":"categoryButton"}
                onClick={()=>this.selectCategory(category.id)}>
                    {category.name}
                    <img className="categoriesButtonIcon"
                    src={this.state.selectedCategories.includes(category.id)?
                    "/images/category_selected.svg":"/images/category_unselected.svg"}/>
                </div>)}
            </div>
            <div className="shopItemWrapper">
                <div className="sortDisplayBar">
                    <span>
                        <select className="selectSorting" name="sort" id="sort" onChange={this.sortChanged}>
                            {this.sorts.map(sort=><option value={sort.value}>{sort.name}</option>)}
                        </select>
                    </span>
                    <span className="displayModeContainer">
                        <span onClick={(() => this.displayChanged("grid"))} id="grid" className={this.state.display==="grid"?"displayModeSelected":"displayMode"}>
                            <img className="displayModeIcon" src={this.state.display==="grid"?"/images/grid_icon_light.svg":"/images/grid_icon_dark.svg"}/>
                            Grid
                        </span>
                        <span onClick={(() => this.displayChanged("list"))} id="list" className={this.state.display==="list"?"displayModeSelected":"displayMode"}>
                            <img className="displayModeIcon" src={this.state.display==="list"?"/images/list_icon_light.svg":"/images/list_icon_dark.svg"}/>
                            List
                        </span>
                    </span>    
                </div>
                <div className={this.state.display==="grid"?"gridItemContainer":"listItemContainer"}>
                    {this.state.items.map(item=><ItemElement link={"/item?id="+item.id} item={item} type={this.state.display}/>)}
                </div>
                <div className="width10">
                    <div className={this.state.loadMore?"loadEnabled":"loadDisabled"} onClick={this.load}>Load More</div>
                </div>
            </div>
        </div>
        );
    }
}

Search.contextType=AuthContext;
export default Search