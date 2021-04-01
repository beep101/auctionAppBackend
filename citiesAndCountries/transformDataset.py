import json

source=open("worldcities.csv","r", encoding='utf-8', errors='ignore')
records=source.readlines()
records.pop(0)
transformed_data={}
for record in records:
    splitted=record.split(",")
    city,country=splitted[1][1:-1],splitted[4][1:-1]
    try:
        transformed_data[country].append(city)
    except KeyError:
        transformed_data[country]=[city]
destination=open("countriesAndCities.json","w+")
destination.write(json.dumps(transformed_data))
    
          
