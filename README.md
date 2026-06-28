# 🍴 FoodJava — Sistema de Pedidos para Restaurante

Projeto Final da disciplina de **Programação Orientada a Objetos**  
IFPB — Campus Monteiro | Curso Superior de Análise e Desenvolvimento de Sistemas

---

## 👥 Integrantes da Equipe

| Nome | Matrícula |
|------|-----------|
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |

---

## 📋 Sobre o Sistema

O **FoodJava** é um sistema de gerenciamento de pedidos para um único restaurante. Permite:

- **Gerente**: configurar o restaurante, gerenciar o cardápio (CRUD + importação JSON), visualizar todos os pedidos e avançar o status de cada um.
- **Cliente**: se cadastrar, navegar pelo cardápio, montar um carrinho e acompanhar o status dos pedidos em tempo real.

---

## ✅ Conceitos de POO aplicados

| Conceito | Onde é aplicado |
|----------|----------------|
| **Herança** | `Usuario` ← `Gerente` / `Cliente` |
| **Classe Abstrata** | `Usuario` (método `getPerfil()` abstrato) |
| **Polimorfismo** | `login()` retorna `Usuario`; tratado como `Gerente` ou `Cliente` em runtime |
| **Encapsulamento** | Todos os atributos são privados com getters/setters |
| **Interface** | `JsonDeserializer<>` do Gson (implementada em lambdas nos repositories) |
| **Exceções personalizadas** | Pacote `exception` com 9 exceções customizadas |
| **Enum** | `Categoria`, `StatusPedido` |

---

## 🚀 Pré-requisitos

- **Java 17+** (verifique com `java -version`)
- **Maven 3.8+** (verifique com `mvn -version`)
- Conexão com a internet na primeira execução (para baixar dependências)

---

## ▶️ Como executar

1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd FoodJava
   ```

2. Execute o projeto:
   ```bash
   mvn javafx:run
   ```

> Na **primeira execução**, o sistema abre a tela de configuração do restaurante.  
> Nas execuções seguintes, abre direto na tela de **login**.

---

## 📁 Estrutura do projeto

```
FoodJava/
├── src/main/java/br/edu/ifpb/ads/foodjava/
│   ├── model/          # Entidades: Usuario, Gerente, Cliente, Pedido, ItemCardapio...
│   ├── view/           # Telas JavaFX (código Java puro)
│   ├── controller/     # Lógica de negócio
│   ├── repository/     # Persistência em JSON com Gson
│   ├── exception/      # Exceções personalizadas
│   └── util/           # Validador (CPF/CNPJ), UI helper, Caminhos
├── src/main/resources/
│   └── css/estilo.css  # Estilo visual das telas
├── exemplos-json/
│   └── cardapio_exemplo.json  # Arquivo de exemplo para importação
├── pom.xml
└── README.md
```

---

## 📂 Importação de Cardápio via JSON

Use o arquivo `exemplos-json/cardapio_exemplo.json` como modelo.  
No painel do gerente → **Gerenciar Cardápio** → **Importar JSON**.

Categorias válidas: `ENTRADA`, `PRATO_PRINCIPAL`, `SOBREMESA`, `BEBIDAS`.

---

## 🗂️ Dados persistidos

Os arquivos JSON são gerados automaticamente em runtime:

```
data/
├── restaurante.json
├── clientes.json
├── cardapio.json
└── pedidos.json
uploads/   ← imagens dos itens do cardápio
```
