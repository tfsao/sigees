CREATE TABLE IF NOT EXISTS produtos (
    id serial,
    nome varchar(255) not null,
    preco numeric not null,
    quantidade integer not null,
    primary key (id)
);
