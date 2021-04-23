import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { addWishToWishlist, delWishFromWishlist } from '../apiConsumer/wishlistConsumer';
import AuthContext from '../context';

function ListItemButtons(props){
    const [container]=useState(props.mode==="grid"?"gridItemButtonContainer":"listItemButtonContainer");
    const [buttons]=useState(props.mode==="grid"?"gridItemButton":"itemWishlistButton");
    const [isWish,setIsWish]=useState(false);

    useEffect(()=>{
        setIsWish(context.wishes.some(w => w.item.id === props.item.id));
    },[])

    const onWishClick=()=>{
        if(isWish){
            let wishes=context.wishes.filter(w=>w.item.id===props.item.id);
            delWishFromWishlist({id:wishes[0].id},context.jwt,(success,data)=>{
                if(success){
                    context.removeWish(data);
                    setIsWish(false);
                }
            })
        }else{
            addWishToWishlist({item:{id:props.item.id}},context.jwt,(success,data)=>{
                if(success){
                    context.addWish(data);
                    setIsWish(true);
                }                
            })
        }
    }

    const context=useContext(AuthContext);
    return(
            <div className={container}>
                {props.item.seller.id==context.user.jti?  
                    <div className={buttons}>
                        Wishlist
                        <img className="socialImg" src={props.mode==="grid"?"/images/watchlist_dark.svg":"/images/watchlist.svg"}/>
                    </div>
                    :
                    <div className={isWish?`${buttons}Selected pointer`:`${buttons} pointer`}
                        onClick={onWishClick}>
                        Wishlist
                        <img className="socialImg" src={isWish?"/images/watchlist_ppl.svg":props.mode==="grid"?"/images/watchlist_dark.svg":"/images/watchlist.svg"}/>
                    </div>
                }

                <Link to={props.bidLink} className={`${buttons} pointer`}>
                    Bid
                    <img className="socialImg" src={props.mode==="grid"?"/images/bid_dark.svg":"/images/bid.svg"}/>
                </Link>
            </div>
    )
}

export default ListItemButtons