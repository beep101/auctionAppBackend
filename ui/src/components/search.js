import React from 'react';
import AuthContext from '../context';
import ItemElement from './itemELement';
import { Link } from 'react-router-dom';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {searchItems} from '../apiConsumer/itemFetchConsumer' 

class Search extends React.Component{

    constructor(props){
        super(props);
        this.state={
            items:[],
            load: 0,
            loadMore: true,
            categories:[],
            selectedCategories:[],
            searchText:''
        }
    }

    load=()=>{
        searchItems(this.context.searchText,this.state.selectedCategories,this.state.load,12,(success, data)=>{
            if(success){
                console.log(data);
                console.log(this.state)
                if(data.length==0){
                    this.setState({['loadMore']:false});
                }
                if(this.state.load!=0){
                    this.setState({['items']:[...this.state.items,...data]});
                }else{
                    this.setState({['items']:data});
                }
                this.setState({['load']:this.state.load+1});
            }
        })
    }

        
    componentDidMount=()=>{
        this.loadCategories();
        this.load();
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
        }else{
            if(this.state.selectedCategories.includes(cat)){
                this.setState({['selectedCategories']:this.state.selectedCategories.filter((x)=>x!=cat)});
            }else{
                this.setState({['selectedCategories']:[...this.state.selectedCategories,cat]})
            }
        }
        this.setState({['loadMore']:true});
        this.setState({['load']:0});
        this.load()
    }

    componentDidUpdate() {
        if(this.context.searchText!=this.state.searchText){
            this.setState({['loadMore']:true});
            this.setState({['load']:0});
            this.setState({['searchText']:this.context.searchText});
            this.load();
        }
    }

    render(){
        return(
        <div class="shopContainer">
           
            <div class="categoriesList">
                <div class={this.state.selectedCategories.length==0?"categoryButtonSelected":"categoryButton"} onClick={()=>this.selectCategory("")}>all categories</div>
                {this.state.categories.map(category=><div class={this.state.selectedCategories.includes(category.id)?"categoryButtonSelected":"categoryButton"} onClick={()=>this.selectCategory(category.id)}>{category.name}</div>)}
            </div>
            <div class="shopItemWrapper">
                <div class="gridItemContainer">
                    <div style={{visibility: 'hidden'}}>{this.context.searchText}</div>
                    {this.state.items.map(item=><Link to={"/item?id="+item.id}><ItemElement item={item} type="grid"/></Link>)}
                </div>
                <div class="width10">
                    <div class={this.state.loadMore?"loadEnabled":"loadDisabled"} onClick={this.load}>Load More</div>
                </div>
            </div>
        </div>
        );
    }
}

Search.contextType=AuthContext;
export default Search