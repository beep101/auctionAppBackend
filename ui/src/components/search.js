import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 

class Search extends React.Component{

    constructor(props){
        super(props);
        this.loadCount=0;
        this.searchText="";
        this.selectedCategories=[];
        this.sort="default";

        this.sorts=[
            {value:"default",name:"Default Sort"},
            {value:"newness",name:"Sort By Newness"},
            {value:"priceAsc",name:"Sort By Price Ascending"},
            {value:"priceDesc",name:"Sort By Price Descending"}
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
        this.searchText=this.context.searchText;
        this.loadCategories();
        this.load();
        this.context.setSearchCallback((text)=>{this.textChanged(text);});
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
        searchItems(this.searchText,this.selectedCategories,this.loadCount,12,this.sort,(success, data)=>{
            if(success){
                if(data.length===0){
                    this.setState({['loadMore']:false});
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
        if(cat===""){
            this.setState({['selectedCategories']:[]});
            this.selectedCategories=[];
        }else{
            if(this.state.selectedCategories.includes(cat)){
                this.setState({['selectedCategories']:this.state.selectedCategories.filter((x)=>x!=cat)});
                this.selectedCategories=this.selectedCategories.filter((x)=>x!=cat);
            }else{
                this.setState({['selectedCategories']:[...this.state.selectedCategories,cat]})
                this.selectedCategories.push(cat);
            }
        }
        this.loadCount=0;
        this.setState({['loadMore']:true});
        this.load()
    }

    render(){
        return(
        <div className="shopContainer">
           
            <div className="categoriesList">
                <div className="categoryButtonTitle">Product Categories</div>
                <div className={this.state.selectedCategories.length===0?"categoryButtonSelected":"categoryButton"}
                    onClick={()=>this.selectCategory("")}>all categories</div>
                {this.state.categories.map(category=><div
                    className={this.state.selectedCategories.includes(category.id)?"categoryButtonSelected":"categoryButton"}
                    onClick={()=>this.selectCategory(category.id)}>{category.name}</div>)}
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