import {get,post} from './apiConsumer';

export function getBids(itemId,handler){
    get(`items/${itemId}/bids`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            console.log(error);
            handler(false,null);
        }
    );
}

export function getBidsLimited(itemId,limit,handler){
    get(`items/${itemId}/bids?limit=${limit}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            console.log(error);
            handler(false,null);
        }
    );
}

export function addBid(bid,token,handler){
    post("bids",bid,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            console.log(error);
            handler(false,null);
        }
    );
}