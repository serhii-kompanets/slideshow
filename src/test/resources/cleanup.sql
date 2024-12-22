set search_path to slideshow;

begin transaction;

truncate table images CASCADE ;
alter sequence images_id_seq restart with 1;

truncate table proof_of_play CASCADE ;
alter sequence proof_of_play_id_seq restart with 1;

truncate table slideshow CASCADE ;
alter sequence slideshow_id_seq restart with 1;

truncate table slideshow_image CASCADE ;
alter sequence slideshow_image_id_seq restart with 1;

commit;