import {get} from './apiConsumer'

export function getAllCategories(handler){
    get("categories").then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            console.log(error);
            handler(false,null);
        }
    );
}