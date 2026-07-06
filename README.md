# 🍴 FoodJava

Sistema de gerenciamento de pedidos para restaurante — Projeto Final de Programação Orientada a Objetos.

**IFPB — Campus Monteiro | Análise e Desenvolvimento de Sistemas**

---

## 👥 Integrantes

| Nome | Matrícula |
|------|-----------|
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |

---

## ⚙️ Pré-requisitos

- Java 17+
- Maven 3.8+

---

## ▶️ Como executar

```bash
git clone <URL_DO_REPOSITORIO>
cd FoodJava
mvn javafx:run
```

> **Primeira execução:** o sistema abre a tela de configuração do restaurante. Nas seguintes, vai direto para o login.

---

## 🔑 Acesso

**Gerente:** criado na tela de Configuração Inicial (primeira execução).  
**Cliente:** clique em "Criar conta" na tela de login.

> Para redefinir o sistema, delete a pasta `data/` e execute novamente.  
> CNPJ válido para testes: `11.222.333/0001-81`

---

## 🗂️ Estrutura de Pacotes

```
src/main/java/br/edu/ifpb/ads/foodjava/
├── Main.java
├── model/          # Entidades: Usuario (abstrata), Gerente, Cliente, Pedido, ItemCardapio...
├── controller/     # Lógica de negócio (AuthController, PedidoController...)
│   └── fxml/       # Controllers das telas FXML
├── repository/     # Persistência em JSON com Gson
├── exception/      # 9 exceções personalizadas
└── util/           # Validador (CPF/CNPJ), Sessao, UI, Caminhos

src/main/resources/
├── fxml/           # Telas editáveis no Scene Builder
├── css/estilo.css
└── images/placeholder.png
```

---

## 📦 Importação de Cardápio

Use o arquivo `exemplos-json/cardapio_exemplo.json` como modelo.  
No sistema: **Gerenciar Cardápio → Importar JSON**.

Categorias válidas: `ENTRADA`, `PRATO_PRINCIPAL`, `SOBREMESA`, `BEBIDAS`

---

## 🧱 Conceitos de POO Aplicados

| Conceito | Onde |
|----------|------|
| Herança | `Usuario` ← `Gerente` / `Cliente` |
| Classe Abstrata | `Usuario` com método abstrato `getPerfil()` |
| Polimorfismo | `login()` retorna `Usuario`; tipo real define a tela de destino |
| Encapsulamento | Todos os atributos do `model` são `private` |
| Interface | `JsonDeserializer<T>` do Gson; `Initializable` do JavaFX |
| Enum | `Categoria` e `StatusPedido` (com método `proximo()`) |
| Exceções | 9 exceções checked no pacote `exception` |
| Singleton | `Sessao` — mantém o usuário logado entre as telas |
| MVC + Repository | Model / FXML (View) / Controller de negócio / Repository |

---

## 🚫 Restrições

Sem Spring Boot, Hibernate ou banco de dados — persistência exclusivamente via JSON com Gson, conforme exigido pelo enunciado.
