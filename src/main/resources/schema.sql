
create table empresa(
	id varchar(40) primary key,
    nome varchar(45) not null,
	email varchar(45) unique not null,
	senha varchar(100) not null,
	CNPJ varchar(45) unique not null,
    `version` long
);

create table usuario(
	id varchar(40) primary key,
	nome varchar(45) not null,
	email varchar(45) unique not null,
	senha varchar(100) not null,
    `version` long
);

create table avaliacao (
	id integer auto_increment primary key,
	titulo varchar(45) not null,
    criado_em datetime default current_timestamp,
    texto text not null,
	nota real,
    empresa_cnpj varchar(45),
	usuario_id varchar(40),
	foreign key (usuario_id) references usuario(id),
    foreign key (empresa_cnpj) references empresa(cnpj)
);

create table comentario (
	id integer auto_increment primary key,
    texto text not null,
    criado_em datetime default current_timestamp,
    autor_id varchar(40) not null,
    avaliacao_id integer,
    comentario_id integer,
    constraint fk_autor foreign key (autor_id) references usuario(id),
    constraint fk_avaliacao foreign key (avaliacao_id) references avaliacao(id) on delete cascade,
    constraint fk_comentario foreign key (comentario_id) references comentario(id) on delete cascade
);

create table resposta_empresa (
    id integer auto_increment primary key,
    resposta text not null,
    data_resposta datetime default current_timestamp,
    empresa_cnpj varchar(45),
    avaliacao_id integer,
    foreign key (empresa_cnpj) references empresa(cnpj),
    foreign key (avaliacao_id) references avaliacao(id)
);

create table likes (
	comentario_id integer,
	avaliacao_id integer,
    usuario_id varchar(36),
    constraint fk_comentario_like foreign key (comentario_id) references comentario(id) on delete cascade,
    constraint fk_avaliacao_like foreign key (avaliacao_id) references avaliacao(id) on delete cascade,
    constraint fk_usuario_like foreign key (usuario_id) references usuario(id) on delete set null
);