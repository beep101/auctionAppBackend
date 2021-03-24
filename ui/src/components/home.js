import React from 'react';
import { getLastChance, getNewArrivals, getFeaturedItem } from '../apiConsumer/itemFetchConsumer';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import ItemElement from './itemELement';
import FeaturedItem from './featuredItem'
import { Link } from 'react-router-dom';


class Home extends React.Component{
    constructor(props){
        super(props);

        this.state={
            items:[],
            categories:[],
            activeTab:""
        }
    }

    componentDidMount(){
        this.loadCategories();
        this.loadFeaturedItem();
        this.lastChanceClick();
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

    loadFeaturedItem=()=>{
        getFeaturedItem((success,data)=>{
            if(success){
                this.setState({['featured']:data});
            }else{
                console.log("Cannot load featured item")
            }
        })
    }

    lastChanceClick=()=>{
        getLastChance(0,6,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'lastChance'});
            }else{
                console.log("Cannot fetch data");
            }
        });
    }

    newArrivalsClick=()=>{
        getNewArrivals(0,6,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'newArrivals'});
            }else{
                console.log("Cannot fetch data");
            }
        });     
    }

    render(){
        return(
            <div className="homeContainer">
                <div className="catsAndFtdBackground">
                <div className="catsAndFtd">
                    <div className="homeCategoriesContainer">
                        <div className="homeCategoryTitle">CATEGORIES</div>
                        {this.state.categories.map(category=><Link to={`/shop?category=${category.id}`}><div  className="homeCategoryButton">{category.name}</div></Link>)}
                        <Link to="allCategories"><div className="homeCategoryAllButton">All Categories</div></Link>
                    </div>
                    {this.state.featured&&<FeaturedItem item={this.state.featured}/>}
                </div>
                </div>
                <div  className="homePageNavigator">
                    <span className={this.state.activeTab==="lastChance"?"highlightLinkStyle highlightLinkStyleBorder":"linkStyle"} onClick={this.lastChanceClick}>Last Chance</span>
                    <span className={this.state.activeTab==="newArrivals"?"highlightLinkStyle highlightLinkStyleBorder":"linkStyle"} onClick={this.newArrivalsClick}>New Arrivals</span>
                </div>
                <div className="gridItemContainer">
                    {this.state.items.map(item=><ItemElement link={"/item?id="+item.id} item={item} type="grid"/>)}
                </div>
            </div>
        )
    }

}

export default Home