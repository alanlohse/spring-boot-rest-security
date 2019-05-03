
    create table tb_order (
        id_order int8 not null,
        nm_address varchar(200) not null,
        dt_confirmation timestamp,
        cd_status varchar(10) not null,
        id_store int8,
        primary key (id_order)
    );

    create table tb_order_item (
        id_order_item int8 not null,
        ds_item varchar(200) not null,
        nr_quantity numeric(10, 3) not null,
        nr_unit_price numeric(10, 2) not null,
        id_order int8 not null,
        primary key (id_order_item)
    );

    create table tb_payment (
        id_order_item int8 not null,
        nr_credit_card varchar(20) not null,
        dt_payment timestamp,
        cd_status varchar(10) not null,
        id_order int8 not null,
        primary key (id_order_item)
    );

    create table tb_store (
        id_store int8 not null,
        nm_address varchar(200),
        nm_store varchar(100) not null,
        primary key (id_store)
    );

    create table tb_user (
        nm_username varchar(20) not null,
        nm_password varchar(100) not null,
        id_store int8,
        primary key (nm_username)
    );

    create index ix_orderitem_dsitem on tb_order_item (ds_item);

    create index ix_store_nmstore on tb_store (nm_store);

    alter table tb_order 
        add constraint fk_store_to_order 
        foreign key (id_store) 
        references tb_store;

    alter table tb_order_item 
        add constraint fk_order_to_orderitem 
        foreign key (id_order) 
        references tb_order;

    alter table tb_payment 
        add constraint fk_order_to_payment 
        foreign key (id_order) 
        references tb_order;

    alter table tb_user 
        add constraint fk_store_to_user 
        foreign key (id_store) 
        references tb_store;

    create sequence hibernate_sequence;

    insert into tb_user (nm_username, nm_password) values ('admin', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=');
    
    