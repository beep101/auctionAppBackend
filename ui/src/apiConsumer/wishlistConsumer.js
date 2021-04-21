import {get,post,del} from './apiConsumer';

export function getAllWishes(token,handler){
    get("wishlist",{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );
}

export function addWishToWishlist(wish,token,handler){
    post("wishlist",wish,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );
}

export function delWishFromWishlist(wish,token,handler){
    del(`wishlist?wishId=${wish.id}`,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );
}