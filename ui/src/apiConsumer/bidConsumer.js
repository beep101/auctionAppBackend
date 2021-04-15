import {get,post} from './apiConsumer';

export function getBids(itemId,handler){
    get(`items/${itemId}/bids`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,error);
        }
    );
}

export function getBidsLimited(itemId,limit,handler){
    get(`items/${itemId}/bids?limit=${limit}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,error);
        }
    );
}

export function addBid(bid,token,handler){
    post("bids",bid,{headers:{'Authorization' : 'Bearer '+token}}).then(
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