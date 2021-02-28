import React from 'react';
import {getAllCategories} from '../apiConsumer/categoryConsumer'
import {getItems,getItemsByCategory} from '../apiConsumer/itemFetchConsumer'
import ItemElement from './itemELement'

class Shop extends React.Component{

    constructor(props){
        super(props);

        //set props from route
        //categoryId specifficaly

        this.state={
            items:[],
            categories:[],
            category:"",
            loadMore: true,
            page: 0
        }
    }

    
    componentDidMount=()=>{
        this.loadCategories();
        this.setState({['page']:0});
        this.loadItems(false);
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

    loadMore=()=>{
        this.setState({['page']:this.state.page++});
        this.loadItems(true);
    }

    selectCategory=(categoryId)=>{
        this.setState({['category']: categoryId.toString()});
        this.setState({['page']:0});
        this.loadItems(false);
    }

    loadItems(append){
        if(this.state.category==""){
            getItems(this.state.page,12,(success,data)=>{
                if(success){
                    if(append){
                        this.setState({['items']:[...this.state.items,...data]});
                        if(data.length==0){
                            this.setState({['loadMore']:false});
                        }
                    }else{
                        this.setState({['items']:data});
                        this.setState({['loadMore']:true});
                    }
                }else{
                    console.log("Cannot fetch items")
                }
            });
        }else{
            getItemsByCategory(this.state.category,this.state.page,12,(success,data)=>{
                if(success){
                    if(append){
                        this.setState({['items']:[...this.state.items,...data]});
                        if(data.length==0){
                            this.setState({['loadMore']:false});
                        }
                    }else{
                        this.setState({['items']:data});
                        this.setState({['loadMore']:true});
                    }
                }else{
                    console.log("Cannot fetch items")
                }
            });
        }
    }

    render(){
        return(
        <div class="shopContainer">
            <div class="categoriesList">
                <div class={this.state.category==""?"categoryButtonSelected":"categoryButton"} onClick={()=>this.selectCategory("")}>all categories</div>
                {this.state.categories.map(category=><div class={this.state.category==category.id?"categoryButtonSelected":"categoryButton"} onClick={()=>this.selectCategory(category.id)}>{category.name}</div>)}
            </div>
            <div class="shopItemWrapper">
                <div class="gridItemContainer">
                    {this.state.items.map(item=><ItemElement item={item} type="grid"/>)}
                </div>
                <div class="width10">
                    <div class={this.state.loadMore?"loadEnabled":"loadDisabled"} onClick={this.loadMore}>Load More</div>
                </div>
            </div>
        </div>
        );
    }
}

export default Shop