import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import { Link } from 'react-router-dom';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 

class Search extends React.Component{

    constructor(props){
        super(props);
        this.loadCount=0;
        this.searchText="";
        this.selectedCategories=[];
        this.state={
            items:[],
            loadMore: true,
            categories:[],
            selectedCategories:[]
        }
    }
        
    componentDidMount=()=>{
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

    load=()=>{
        searchItems(this.searchText,this.selectedCategories,this.loadCount,12,(success, data)=>{
            if(success){
                if(data.length==0){
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
        if(cat==""){
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
                <div className={this.state.selectedCategories.length==0?"categoryButtonSelected":"categoryButton"}
                    onClick={()=>this.selectCategory("")}>all categories</div>
                {this.state.categories.map(category=><div
                    className={this.state.selectedCategories.includes(category.id)?"categoryButtonSelected":"categoryButton"}
                    onClick={()=>this.selectCategory(category.id)}>{category.name}</div>)}
            </div>
            <div className="shopItemWrapper">
                <div className="gridItemContainer">
                    {this.state.items.map(item=><Link to={"/item?id="+item.id}><ItemElement item={item} type="grid"/></Link>)}
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