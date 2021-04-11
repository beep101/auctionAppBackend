import {get} from './apiConsumer'
import { getLinks } from './imageLinker';

export function getLastChance(page,count,handler){
    get(`items/lastChance?page=${page}&count=${count}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,null);
        }
    );
}

export function getNewArrivals(page,count,handler){
    get(`items/newArrivals?page=${page}&count=${count}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItemsByCategory(categoryId,page,count,handler){
    get(`items/category/${categoryId}?page=${page}&count=${count}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItems(page,count,handler){
    get(`items?page=${page}&count=${count}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function searchItems(term,categories,subcategories,minPrice,maxPrice,page,count,sort,handler){
    let searchString=`items/search?page=${page}&count=${count}&term=${term}&categories=${categories.join(',')}&subcategories=${subcategories.join(',')}&sort=${sort}`
    if(minPrice)
        searchString=`${searchString}&minPrice=${minPrice}`;
    if(maxPrice)
        searchString=`${searchString}&maxPrice=${maxPrice}`;
    get(searchString).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function getItemById(id,handler){
    get(`items/${id}`).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,"")
        }
    )
}

export function getFeaturedItem(handler){
    get('items/featured').then(
        (response)=>{
            handler(true,response.data)
        },
        (error)=>{
            handler(false,"")
        }
    );
}

export function getPriceHistogram(handler){
    get('items/priceHistogram').then(
        (response)=>{
            handler(true,response.data)
        },
        (error)=>{
            handler(false,"")
        }
    )
}

export function getActiveItems(token,handler){
    get('items/active',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data)
        },
        (error)=>{
            handler(false,"")
        }
    )
}

export function getInactiveItems(token,handler){
    get('items/inactive',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data)
        },
        (error)=>{
            handler(false,"")
        }
    )
}

export function getBiddedItems(token, handler){
    get('items/bidded',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data)
        },
        (error)=>{
            handler(false,"")
        }
    )
}
