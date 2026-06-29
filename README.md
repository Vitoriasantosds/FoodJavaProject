# 🍴 FoodJava

Sistema de gerenciamento de pedidos para restaurante desenvolvido como Projeto Final da disciplina de **Programação Orientada a Objetos**.

**IFPB — Campus Monteiro | Curso Superior de Análise e Desenvolvimento de Sistemas**

---

## 👥 Integrantes da Equipe

| Nome | Matrícula |
|------|-----------|
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |
| _(preencher)_ | _(preencher)_ |

---

## 📋 Sobre o Sistema

O FoodJava permite que um restaurante gerencie seu cardápio e pedidos com dois perfis de usuário:

| Perfil | Responsabilidades |
|--------|-------------------|
| **Gerente** | Configura o restaurante na primeira execução, gerencia o cardápio (adicionar, editar, ativar/desativar, excluir, importar via JSON com imagens), visualiza todos os pedidos e avança o status de cada um |
| **Cliente** | Realiza cadastro, navega pelo cardápio com fotos, monta carrinho, confirma pedido e acompanha o status em tempo real |

---

## ✅ Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8** ou superior

Verifique as versões instaladas:

```bash
java -version
mvn -version
```

---

## ▶️ Como executar

**1. Clone o repositório:**

```bash
git clone <URL_DO_REPOSITORIO>
cd FoodJava
```

**2. Execute o projeto:**

```bash
mvn javafx:run
```

> **Primeira execução:** o sistema detecta que nenhum restaurante foi configurado e abre automaticamente a tela de configuração. Preencha os dados do restaurante e crie a conta do gerente.  
> **Execuções seguintes:** o sistema abre diretamente na tela de login.

---

## 🔑 Acesso ao sistema

### Gerente
O acesso do gerente é criado na **tela de Configuração Inicial** (primeira execução). Use o e-mail e senha definidos nessa etapa.

> Para redefinir e exibir a configuração inicial novamente, delete a pasta `data/` na raiz do projeto e execute novamente.

**CNPJ válido para testes:** `11.222.333/0001-81`

### Cliente
Clique em **"Criar conta"** na tela de login e preencha o formulário de cadastro.

**Regras de senha:** mínimo 8 caracteres e ao menos 1 dígito numérico.

---

## 🗂️ Estrutura do Projeto

```
FoodJava/
├── src/
│   └── main/
│       ├── java/br/edu/ifpb/ads/foodjava/
│       │   ├── Main.java                        # Ponto de entrada, navegação entre telas
│       │   ├── model/                           # Entidades do domínio
│       │   │   ├── Usuario.java                 # Classe abstrata base
│       │   │   ├── Gerente.java                 # Herda de Usuario
│       │   │   ├── Cliente.java                 # Herda de Usuario
│       │   │   ├── Restaurante.java
│       │   │   ├── ItemCardapio.java
│       │   │   ├── ItemPedido.java
│       │   │   ├── Pedido.java
│       │   │   ├── Categoria.java               # Enum: ENTRADA, PRATO_PRINCIPAL, SOBREMESA, BEBIDAS
│       │   │   └── StatusPedido.java            # Enum: fluxo completo de status
│       │   ├── controller/                      # Lógica de negócio (sem dependência de JavaFX)
│       │   │   ├── AuthController.java
│       │   │   ├── RestauranteController.java
│       │   │   ├── CardapioController.java
│       │   │   ├── PedidoController.java
│       │   │   └── fxml/                        # Controllers vinculados às telas FXML
│       │   │       ├── ConfiguracaoInicialController.java
│       │   │       ├── LoginController.java
│       │   │       ├── CadastroClienteController.java
│       │   │       ├── CardapioClienteController.java
│       │   │       ├── HistoricoClienteController.java
│       │   │       ├── PainelGerenteController.java
│       │   │       ├── GerenciarCardapioController.java
│       │   │       └── EditarItemController.java
│       │   ├── repository/                      # Persistência em JSON com Gson
│       │   │   ├── RestauranteRepository.java
│       │   │   ├── ClienteRepository.java
│       │   │   ├── CardapioRepository.java
│       │   │   └── PedidoRepository.java
│       │   ├── exception/                       # Exceções personalizadas
│       │   │   ├── UsuarioDuplicadoException.java
│       │   │   ├── SenhaInvalidaException.java
│       │   │   ├── DocumentoInvalidoException.java
│       │   │   ├── PrecoInvalidoException.java
│       │   │   ├── StatusInvalidoException.java
│       │   │   ├── CancelamentoNaoPermitidoException.java
│       │   │   ├── ItemVinculadoException.java
│       │   │   ├── ArquivoImportacaoException.java
│       │   │   └── CarrinhoVazioException.java
│       │   └── util/                            # Utilitários
│       │       ├── Validador.java               # Validação de CPF e CNPJ
│       │       ├── Sessao.java                  # Singleton do usuário logado
│       │       ├── Caminhos.java                # Caminhos dos arquivos JSON
│       │       └── UI.java                      # Alertas e cores de status
│       └── resources/
│           ├── fxml/                            # Telas (editáveis no Scene Builder)
│           │   ├── configuracao_inicial.fxml
│           │   ├── login.fxml
│           │   ├── cadastro_cliente.fxml
│           │   ├── cardapio_cliente.fxml
│           │   ├── historico_cliente.fxml
│           │   ├── painel_gerente.fxml
│           │   ├── gerenciar_cardapio.fxml
│           │   └── editar_item.fxml
│           ├── css/
│           │   └── estilo.css                   # Estilo global da aplicação
│           └── images/
│               └── placeholder.png              # Imagem padrão para itens sem foto
├── exemplos-json/
│   └── cardapio_exemplo.json                    # Arquivo de exemplo para importação
├── data/                                        # Gerado em runtime — dados persistidos
│   ├── restaurante.json
│   ├── clientes.json
│   ├── cardapio.json
│   └── pedidos.json
├── uploads/                                     # Gerado em runtime — imagens dos itens
├── pom.xml
└── README.md
```

---

## 🧭 Fluxo de Uso

```
1. Primeira execução
   └── Gerente configura restaurante (nome, CNPJ, endereço, e-mail, senha)

2. Gerente faz login
   └── Gerencia cardápio
       ├── Adiciona itens manualmente (com foto opcional)
       ├── Importa itens via JSON
       ├── Edita, ativa/desativa ou exclui itens

3. Cliente se cadastra e faz login
   └── Navega pelo cardápio (organizado por categoria, com fotos)
       └── Monta carrinho → confirma pedido

4. Gerente avança o status do pedido
   AGUARDANDO_CONFIRMACAO → CONFIRMADO → EM_PREPARO → SAIU_PARA_ENTREGA → ENTREGUE

5. Cliente acompanha o status em "Meus Pedidos"
```

---

## 🖼️ Imagens no Cardápio

### Adicionando imagem manualmente
Na tela **Gerenciar Cardápio**, clique no botão **📁** ao lado do campo de imagem e selecione um arquivo `.png`, `.jpg`, `.jpeg`, `.gif` ou `.webp`.

### Adicionando imagem via importação JSON
Coloque as imagens na pasta `uploads/` na raiz do projeto e referencie no JSON:

```json
{
  "nome": "Baião de Dois",
  "descricao": "Arroz com feijão-verde, queijo coalho e carne-seca",
  "preco": 38.90,
  "categoria": "PRATO_PRINCIPAL",
  "disponivel": true,
  "imagemPath": "uploads/baiao.jpg"
}
```

> Itens sem imagem exibem automaticamente o placeholder `placeholder.png`.

**Categorias válidas para importação:** `ENTRADA`, `PRATO_PRINCIPAL`, `SOBREMESA`, `BEBIDAS`

---

## ⚠️ Exceções Personalizadas

| Exceção | Quando é lançada |
|---------|-----------------|
| `UsuarioDuplicadoException` | E-mail ou CPF já cadastrado |
| `SenhaInvalidaException` | Senha com menos de 8 caracteres ou sem dígito numérico |
| `DocumentoInvalidoException` | CPF ou CNPJ com dígitos verificadores inválidos |
| `PrecoInvalidoException` | Preço de item igual a zero ou negativo |
| `StatusInvalidoException` | Tentativa de avançar status fora da sequência permitida |
| `CancelamentoNaoPermitidoException` | Tentativa de cancelar pedido após confirmação |
| `ItemVinculadoException` | Tentativa de excluir item vinculado a pedido em aberto |
| `ArquivoImportacaoException` | Arquivo JSON ausente, vazio ou com estrutura inválida |
| `CarrinhoVazioException` | Tentativa de confirmar pedido com carrinho vazio |

---

## 🧱 Conceitos de POO Aplicados

| Conceito | Onde é aplicado |
|----------|----------------|
| **Herança** | `Usuario` ← `Gerente` / `Cliente` |
| **Classe Abstrata** | `Usuario` com método abstrato `getPerfil()` |
| **Polimorfismo** | `login()` retorna `Usuario`; tipo real determina a tela de destino |
| **Encapsulamento** | Todos os atributos do `model` são `private` com getters/setters |
| **Interface** | `JsonDeserializer<T>` do Gson (implementada via lambda nos repositories); `Initializable` do JavaFX nos controllers FXML |
| **Enum** | `Categoria` e `StatusPedido` (com método `proximo()` encapsulando o fluxo) |
| **Exceções personalizadas** | 9 exceções checked no pacote `exception` |
| **Singleton** | `Sessao` — mantém o usuário logado entre as telas |
| **Padrão MVC + Repository** | Separação clara entre model, view (FXML), controller de negócio e repositório de dados |

---

## 🔧 Tecnologias e Dependências

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 17+ | Linguagem principal |
| JavaFX | 21 | Interface gráfica |
| Gson | 2.10.1 | Serialização/desserialização JSON |
| Maven | 3.8+ | Gerenciamento de dependências e build |
| Scene Builder | qualquer | Edição visual dos arquivos FXML (opcional) |

---

## 🎨 Editando as Telas com Scene Builder

Todos os arquivos `.fxml` em `src/main/resources/fxml/` podem ser abertos e editados visualmente no **Scene Builder**:

1. Abra o Scene Builder
2. **File → Open** → selecione o arquivo `.fxml` desejado
3. O stylesheet pode ser vinculado em **Properties → Stylesheets → `../css/estilo.css`**

Cada tela já tem seu `fx:controller` configurado — o Scene Builder reconhece automaticamente os campos `@FXML` e os métodos de evento.

---

## 📦 Arquivo de Exemplo para Importação

O arquivo `exemplos-json/cardapio_exemplo.json` contém 9 itens prontos para importar, cobrindo todas as categorias. Use-o para testar a funcionalidade de importação em **Gerenciar Cardápio → Importar JSON**.

---

## 🚫 Restrições do Projeto

- Não utiliza Spring Boot, Hibernate ou qualquer framework que abstraia os conceitos de POO
- Persistência exclusivamente via arquivos JSON com Gson
- Sem banco de dados
- Senhas armazenadas em texto puro (fora do escopo da disciplina)
