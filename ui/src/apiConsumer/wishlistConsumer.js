import {get,post,del} from './apiConsumer';

export function getAllWishes(token,handler){
    get("wishlist",{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function addWishToWishlist(wish,token,handler){
    post("wishlist",wish,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function delWishFromWishlist(wish,token,handler){
    del(`wishlist?wishId=${wish.id}`,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}