insert into categories(name) values('men');
insert into categories(name) values('women');
insert into categories(name) values('kids');
insert into categories(name) values('accessories');
insert into categories(name) values('home');
insert into categories(name) values('art');
insert into categories(name) values('computers');

insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('Sunglasses','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elementum ligula sed tortor sollicitudin semper. Nullam interdum odio et tincidunt sagittis. Morbi metus erat, vulputate non turpis id, pellentesque faucibus nibh. Donec dapibus turpis eu dolor dapibus porta. Phasellus eu lectus ut urna pulvinar elementum. Quisque non imperdiet lorem. Nulla.',
					50.00,now()::timestamp - interval '2' day,now()::timestamp + interval '2' day,1,4);
					
insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('Chair','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor lacus ut felis sodales aliquam. Sed eu porta felis, vel tempor risus. Aliquam erat volutpat. Praesent in finibus nunc. Pellentesque turpis tellus, tempus in ipsum id, placerat lobortis.',
					20.00,now()::timestamp - interval '3' day,now()::timestamp + interval '1' day,1,5);

insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('Marvel 1602 Comic','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor lacus ut felis sodales aliquam. Sed eu porta felis, vel tempor risus. Aliquam erat volutpat. Praesent in finibus nunc. Pellentesque turpis tellus, tempus in ipsum id, placerat lobortis.',
					30.00,now()::timestamp,now()::timestamp + interval '7' day,1,6);

insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('Black hoodie','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elementum ligula sed tortor sollicitudin semper. Nullam interdum odio et tincidunt sagittis. Morbi metus erat, vulputate non turpis id, pellentesque faucibus nibh. Donec dapibus turpis eu dolor dapibus porta. Phasellus eu lectus ut urna pulvinar elementum. Quisque non imperdiet lorem. Nulla.',
					40.00,now()::timestamp - interval '1' day,now()::timestamp + interval '6' day,1,1);
					
insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('CRT TV','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor lacus ut felis sodales aliquam. Sed eu porta felis, vel tempor risus. Aliquam erat volutpat. Praesent in finibus nunc. Pellentesque turpis tellus, tempus in ipsum id, placerat lobortis.',
					15.00,now()::timestamp - interval '2' day,now()::timestamp + interval '5' day,2,5);

insert into  items(name,description,startingPrice,startTime,endTime,seller,category)
			values('Nintendo Wii','Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor lacus ut felis sodales aliquam. Sed eu porta felis, vel tempor risus. Aliquam erat volutpat. Praesent in finibus nunc. Pellentesque turpis tellus, tempus in ipsum id, placerat lobortis.',
					50.00,now()::timestamp,now()::timestamp + interval '4' day,2,7);		