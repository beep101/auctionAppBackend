import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 
import {SHOP_LOAD_COUNT} from '../utils/constants'
import Select from 'react-select';
import {SORTING_SELECT_THEME, SORTING_SELECT_STYLES} from '../utils/constants'
import queryString from 'query-string';

class Search extends React.Component{

    constructor(props){
        super(props);
        this.loadCount=0;
        this.searchText="";
        this.selectedCategories=[];
        this.sort="default";

        this.sorts=[
            {value:"default",label:"Default"},
            {value:"newness",label:"Newest first"},
            {value:"timeleft",label:"Time left"},
            {value:"priceAsc",label:"By Price Ascending"},
            {value:"priceDesc",label:"By Price Descending"}
        ]

        this.state={
            items:[],
            loadMore: true,
            categories:[],
            selectedCategories:[],
            display:"grid"
        }
        
        let params = queryString.parse(this.props.location.search)
        let category=parseInt(params['category'])
        if(category){
            this.state['selectedCategories']=[category];
            this.selectedCategories=[category];
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

    sortChanged=(sort)=>{
        if(this.sort!=sort.value){
            this.setState({['loadMore']:true});
            this.loadCount=0;
            this.sort=sort.value;
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
                    <span className='width200px'>
                        <Select options={this.sorts} isSearchable={false} onChange={this.sortChanged}
                                styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={this.sorts[0]}/>
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
                    <div className={this.state.loadMore?"loadEnabled":"loadDisabled"} onClick={()=>this.state.loadMore&&this.load()}>Load More</div>
                </div>
            </div>
        </div>
        );
    }
}

Search.contextType=AuthContext;
export default Search