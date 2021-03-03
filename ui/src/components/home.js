import React from 'react';
import { getLastChance, getNewArrivals } from '../apiConsumer/itemFetchConsumer';
import {getLinks} from '../apiConsumer/imageLinker';
import ItemElement from './itemELement';


class Home extends React.Component{
    constructor(props){
        super(props);

        this.state={
            items:[],
            activeTab:""
        }
    }

    componentDidMount(){
        this.lastChanceClick();
    }

    lastChanceClick=()=>{
        getLastChance(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'lastChance'});
            }else{
                console.log("Cannot fetch data");
            }
        });
    }

    newArrivalsClick=()=>{
        getNewArrivals(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'newArrivals'});
            }else{
                console.log("Cannot fetch data");
            }
        });     
    }

    render(){
        return(
            <div class="homeContainer">
                <div  class="homePageNavigator">
                    <span class={this.state.activeTab=="lastChance"?"highlightLinkStyle":"linkStyle"} onClick={this.lastChanceClick}>Last Chance</span>
                    <span class={this.state.activeTab=="newArrivals"?"highlightLinkStyle":"linkStyle"} onClick={this.newArrivalsClick}>New Arrivals</span>
                </div>
                <hr class="solid"></hr>
                <div class="gridItemContainer">
                    {this.state.items.map(item=><ItemElement item={item} type="grid"/>)}
                </div>
            </div>
        )
    }

}

export default Home