import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 
import {SHOP_LOAD_COUNT} from '../utils/constants'
import Select from 'react-select';
import {SORTING_SELECT_THEME, SORTING_SELECT_STYLES} from '../utils/constants'
import queryString from 'query-string';
import PriceFilter from './priceFilter';
import CategorySubcategoryMenu from './categorySubcategoryMenu';
import FilterItem from './filterItem';

class Search extends React.Component{

    constructor(props){
        super(props);
        this.loadCount=0;
        this.searchText="";
        this.selectedCategories=[];
        this.selectedSubcategories=[];
        this.sort="default";
        this.minPrice=null;
        this.maxPrice=null;

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
            display:"grid"
        }
        
        const category = parseInt((queryString.parse(this.props.location.search))['category']);
        if(category){
            this.category=category;
            this.selectedCategories=[category]
        }else{
            this.category=null;
        }
    }
        
    componentDidMount=()=>{
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
        searchItems(this.searchText,this.selectedCategories,this.selectedSubcategories,this.minPrice,this.maxPrice,this.loadCount,SHOP_LOAD_COUNT,this.sort,(success, data)=>{
            if(success){
                if(data.length<SHOP_LOAD_COUNT){
                    this.setState({['loadMore']:false});
                }else if(data.length==SHOP_LOAD_COUNT){
                    searchItems(this.searchText,this.selectedCategories,this.selectedSubcategories,this.minPrice,this.maxPrice,this.loadCount+1,SHOP_LOAD_COUNT,this.sort,(success, data)=>{
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

    setCategories=(categories)=>{
        this.selectedCategories=categories;
        this.loadCount=0;
        this.setState({['loadMore']:true});
        this.load()
    }

    setSubcategories=(subcategories)=>{
        this.selectedSubcategories=subcategories;
        this.loadCount=0;
        this.setState({['loadMore']:true});
        this.load()
    }
    
    rangeSet=(min,max)=>{
        this.minPrice=min;
        this.maxPrice=max;
        this.loadCount=0;
        this.setState({['loadMore']:true});
        this.load()
    }

    render(){
        return(
        <div className="centeredVerticalFlex">
            <div className="activeFiltersContainer">
                <FilterItem name="filter" disable={()=>{}}/>
            </div>
        <div className="shopContainer">
            <div>
                <CategorySubcategoryMenu preset={this.category} setCategories={this.setCategories} setSubcategories={this.setSubcategories} />
                <PriceFilter rangeSet={this.rangeSet}/>
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
        </div>
        );
    }
}

Search.contextType=AuthContext;
export default Search