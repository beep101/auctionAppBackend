import React, { useEffect, useState } from 'react'
import { getItemsByCategory } from '../apiConsumer/itemFetchConsumer';
import ItemElement from './itemELement';

function RelatedItems(props){

    const [items,setItems]=useState([]);

    useEffect(()=>{
        getItemsByCategory(props.category,0,3,(success,data)=>{
            if(success){
                const filteredData=data.filter(item=>item.id!=props.itemId)
                setItems(filteredData);
            }
            else
                setItems([]);
        })
    },[props.category])

    return(
        <div>
        <div className="relatedItemsTitle">Related products</div>
        <div className="relatedItemsContainer">
            {items.map(item=>(<ItemElement link={"/item?id="+item.id} item={item} type='grid'/>))}
        </div>
        </div>
    )
}

export default RelatedItems