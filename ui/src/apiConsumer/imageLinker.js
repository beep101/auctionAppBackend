import axios from 'axios';

const baseUrl="https://auction.purple.item.pics.s3.eu-central-1.amazonaws.com/"
const query="?list-type=2&&prefix="

export function getLinks(item){
    return new Promise(function(resolve,reject){
        axios.get(`${baseUrl}${query}${item.id}/`).then(
            (response)=>{
                let parser = new DOMParser();
                item.images = Array.from(parser.parseFromString(response.data, "text/xml").getElementsByTagName('Key')).map(elem=>baseUrl+elem.textContent);
                resolve();
            },
            (error)=>{
                reject();
            }
        );
    });
    
}