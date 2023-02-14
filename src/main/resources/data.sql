insert into acces_types(id,acces_type)
values (1,'PUBLIC'),
       (2,'PRIVATE');

insert into click_apps(id,click_apps_enum)
values (1,'PRIORITY'),
       (2,'SPRINTS'),
       (3,'EMAIL'),
       (4,'TAGS'),
       (5,'CUSTOM_FIELDS'),
       (6,'TIME_TRACKING'),
       (7,'DEPENDICY_WARNING'),
       (8,'MILESTONES');

insert into view(id,views)
values (1,'LIST'),
       (2,'BOARD'),
       (3,'CALENDAR'),
       (4,'MAP'),
       (5,'ACTIVITY'),
       (6,'TEAM'),
       (7,'GANTT'),
       (8,'MIND_MAP'),
       (9,'TABLE'),
       (10,'WORKLOAD'),
       (11,'TIMELINE');

insert into type(id,name)
values (1,'Open'),
       (2,'Custom'),
       (3,'Closed');

insert into priority(id,name,color,created_at)
values (1,'Urgent','red',current_timestamp),
       (2,'Hight','yellow',current_timestamp ),
       (3,'Normal','blue',current_timestamp),
       (4,'Low','white',current_timestamp);
