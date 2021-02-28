import {get} from './apiConsumer'
import { getLinks } from './imageLinker';

export function getLastChance(page,count,handler){
    get("items/lastChance/"+page+"/"+count).then(
        (response)=>{
            Promise.allSettled(response.data.map((data) => getLinks(data))).then(()=>{handler(true,response.data)});
        },
        (error)=>{
            console.log(error);
            handler(false,null);
        }
    );
}

export function getNewArrivals(page,count,handler){
    get("items/newArrivals/"+page+"/"+count).then(
        (response)=>{
            Promise.allSettled(response.data.map((data) => getLinks(data))).then(()=>{handler(true,response.data)});
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItemsByCategory(categoryId,page,count,handler){
    get("items/category/"+categoryId+"/"+page+"/"+count).then(
        (response)=>{
            Promise.allSettled(response.data.map((data) => getLinks(data))).then(()=>{handler(true,response.data)});
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItems(page,count,handler){
    get("items/"+page+"/"+count).then(
        (response)=>{
            Promise.allSettled(response.data.map((data) => getLinks(data))).then(()=>{handler(true,response.data)});
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItemById(id,handler){
    get('items/byId/'+id).then(
        (response)=>{
            Promise.resolve(getLinks(response.data)).then(()=>handler(true,response.data));
        },
        (error)=>{
            handler(false,"")
        }
    )
}