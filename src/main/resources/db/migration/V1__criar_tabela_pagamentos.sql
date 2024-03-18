CREATE TABLE pagamentos (
                            id BIGSERIAL NOT NULL,
                            valor NUMERIC(19, 2) NOT NULL,
                            nome VARCHAR(100) DEFAULT NULL,
                            numero VARCHAR(19) DEFAULT NULL,
                            expiracao VARCHAR(7) DEFAULT NULL,
                            codigo VARCHAR(3) DEFAULT NULL,
                            status VARCHAR(255) NOT NULL,
                            forma_de_pagamento_id BIGINT NOT NULL,
                            pedido_id BIGINT NOT NULL,
                            PRIMARY KEY (id)
);
