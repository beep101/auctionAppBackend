import {get,post,defaultErrorHandler} from './apiConsumer';

export function getBids(itemId,handler){
    get(`items/${itemId}/bids`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function getBidsLimited(itemId,limit,handler){
    get(`items/${itemId}/bids?limit=${limit}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function addBid(bid,token,handler){
    post("bids",bid,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}