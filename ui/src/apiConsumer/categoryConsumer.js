import {get,defaultErrorHandler} from './apiConsumer'

export function getAllCategories(handler){
    get("categories").then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}