
USE maindb;

create table empresa(
	id varchar(40) primary key default(uuid()),
    nome varchar(45) not null,
	email varchar(45) unique not null,
	senha varchar(45) not null,
	CNPJ varchar(45) unique not null,
    `version` long
);

create table usuario(
	id varchar(40) primary key default(uuid()),
	nome varchar(45) not null,
	email varchar(45) unique not null,
	senha varchar(45) not null,
    `version` long
);

create table produto_servico (
    id integer auto_increment primary key,
    nome varchar(45) not null,
    empresa_cnpj varchar(45),
    foreign key (empresa_cnpj) references empresa(cnpj)
);

create table avaliacao(
	id integer auto_increment primary key,
	titulo varchar(45) not null,
    criado_em datetime default current_timestamp,
    texto text not null,
	nota real,
    produto_servico_id integer,
    empresa_cnpj varchar(45),
	usuario_id varchar(40),
	foreign key (usuario_id) references usuario(id),
    foreign key (empresa_cnpj) references empresa(cnpj),
    foreign key (produto_servico_id) references produto_servico(id)
);

create table comentario(
	id integer auto_increment primary key,
    texto text not null,
    criado_em datetime default current_timestamp,
    autor_id varchar(40) not null,
    avaliacao_id integer not null,
    constraint fk_autor foreign key (autor_id) references usuario(id),
    constraint fk_avaliacao foreign key (avaliacao_id) references avaliacao(id)
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
