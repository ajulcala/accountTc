# accountTc
cuentas
- Primero se debe ejecutar el servidor Eureka que se encuentra en el repositorio https://github.com/ajulcala/eurekaTc
- Segundo se debe ejecutar el servidor Config Server que se encuentra en el repositorio https://github.com/ajulcala/configServerTc
- Tercero registrar CUSTOMER https://github.com/ajulcala/customerTc

SAVING ACCOUNT

Para registrar un SAVING ACCOUNT en enviaremos por la ruta el dni del CUSTOMER  a traves de un POST:
http://localhost:8091/api/savings/85658965
Estructura para el registro:
{
    "naccount": "123-562-888-365",
    "currency": "soles",
    "maintenance": false,
    "mcommission": 0.0,
    "limittransaction": true,
    "limit": 30,
    "frequency": "MENSUALMENTE",
    "amount": 0.0,
    "card": {
        "number": "125-635-888-1",
        "password": "1234"
    }
}

Si el usuario existe en el microservicio de CUSTOMER procedera a registrarse la cuenta de lo contrario mostrará un mensaje
que registre el usuario primero. Asimismo, no dejara crear otra cuenta si existe una cuenta con el mismo dni.

Buscar por CARD para consultar saldo: http://localhost:8091/api/savings/card a traves de una peticion POST

{
   "number": "125-635-888-1",
   "password": "1234"
}

Para Modificar un SAVING ACCOUNT por ID hacemos una peticion PUT con la estructura antes mencionada:

http://localhost:8091/api/savings/61855fd5c040b66aa9c526d2

y finalmente para eliminar un SAVING ACCOUNT por ID hacemos una peticion DELETE:

http://localhost:8091/api/savings/61855fd5c040b66aa9c526d2


CURRENT ACCOUNT PERSONAL

Para registrar un CURRENT ACCOUNT en enviaremos por la ruta el dni del CUSTOMER  a traves de un POST:
http://localhost:8091/api/currentpersonal/8565877
Estructura para el registro:

{
    "naccount": "124-562-536-99",
    "currency": "SOLES", 
    "type": "CUENTA CORRIENTE PERSONAL",
    "maintenance": true,
    "mcommission": 6.3,
    "limittransaction": false,
    "limit": 0,
    "amount": 0,
    "card":{
        "number": "333-635-89563-88",
        "password": "1234"
    }
}

Si el usuario existe en el microservicio de CUSTOMER procedera a registrarse la cuenta de lo contrario mostrará un mensaje
que registre el usuario primero. Asimismo, no dejara crear otra cuenta si existe una cuenta con el mismo dni.

Buscar por CARD para consultar saldo: http://localhost:8091/api/currentpersonal/card a traves de una peticion POST

{
   "number": "125-635-888-1",
   "password": "1234"
}

Para Modificar un CURRENT ACCOUNT por ID hacemos una peticion PUT con la estructura antes mencionada:

http://localhost:8091/api/currentpersonal/61855fd5c040b66aa9c526d2

y finalmente para eliminar un CURRENT ACCOUNT por ID hacemos una peticion DELETE:

http://localhost:8091/api/currentpersonal/61855fd5c040b66aa9c526d2


CURRENT ACCOUNT EMPRESARIAL

Para registrar un CURRENT ACCOUNT en enviaremos por la ruta el ruc del CUSTOMER  a traves de un POST:
http://localhost:8091/api/currentbusiness/8565877
Estructura para el registro:

{
    "naccount": "124-562-536-99-36",
    "currency": "SOLES",
    "maintenance": true,
    "mcommission": 6.3,    
    "amount": 0.0,
    "card": {
        "number": "333-635-565-01",
        "password": "1234"
    },
"owners":[
        {
        "dni": "56895623",
        "description": "TITULAR PRINCIPAL"
        },
        {
        "dni": "56895677",
        "description": "TITULAR SECUNADRIOS"
        },
        {
        "dni": "56895699",
        "description": "TITULAR SECUNADRIO"
        }
    ],
    "signatories":[
        {
        "dni": "56895623",
        "description": "FIRMANTE PRINCIPAL"
        },
        {
        "dni": "56895677",
        "description": "FIRMANTE SECUNADRIOS"
        },
        {
        "dni": "56895699",
        "description": "FIRMANTE SECUNADRIO"
        }
    ]
}

Buscar por CARD para consultar saldo: http://localhost:8091/api/currentbusiness/card a traves de una peticion POST

{
   "number": "333-635-565-01",
   "password": "1234"
}


Para Modificar un CURRENT ACCOUNT por ID hacemos una peticion PUT con la estructura antes mencionada:

http://localhost:8091/api/currentbusiness/61855fd5c040b66aa9c526d2

y finalmente para eliminar un CURRENT ACCOUNT por ID hacemos una peticion DELETE:

http://localhost:8091/api/currentbusiness/61855fd5c040b66aa9c526d2


FIXED TERM ACCOUNT

Para registrar un FIXED TERM ACCOUNT en enviaremos por la ruta el dni del CUSTOMER  a traves de un POST:
http://localhost:8091/api/fixed/85658965

{
    "naccount": "111-562-536-77",
    "currency": "soles",
    "maintenance": false,
    "mcommission": 0,
    "limittransaction": true,
    "limit": 1,
    "frequency":"MENSUALMENTE",
    "daypay": "24 DE CADA MES",
    "amount": 0,
    "card":{
        "number": "111-635-89563-77",
        "password": "1234"
    }
}

Si el usuario existe en el microservicio de CUSTOMER procedera a registrarse la cuenta de lo contrario mostrará un mensaje
que registre el usuario primero. Asimismo, no dejara crear otra cuenta si existe una cuenta con el mismo dni.

Buscar por CARD para consultar saldo: http://localhost:8091/api/fixed/card a traves de una peticion POST

{
   "number": "125-635-888-1",
   "password": "1234"
}

Para Modificar un FIXED TERM ACCOUNT por ID hacemos una peticion PUT con la estructura antes mencionada:

http://localhost:8091/api/fixed/61855fd5c040b66aa9c526d2

y finalmente para eliminar un FIXED TERM ACCOUNT por ID hacemos una peticion DELETE:

http://localhost:8091/api/sfixed/61855fd5c040b66aa9c526d2
