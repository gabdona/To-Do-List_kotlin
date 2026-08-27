# To-Do List App (Lista de Tarefas)

## Descrição do Projeto e Objetivo
Este é um aplicativo de Lista de Tarefas (To-Do List) desenvolvido para Android. O objetivo da aplicação é permitir que o usuário gerencie suas atividades diárias através de operações básicas de CRUD (Criar, Ler, Atualizar e Deletar). O projeto foi construído com o intuito de aplicar e demonstrar as melhores práticas da arquitetura moderna recomendada pelo Google para o desenvolvimento Android.

## 🛠Tecnologias Utilizadas
O aplicativo foi desenvolvido utilizando as seguintes tecnologias e bibliotecas:
* **Kotlin:** Linguagem de programação principal.
* **Jetpack Compose:** Kit de ferramentas moderno para construção de UI nativa e declarativa.
* **Room:** Biblioteca de persistência para abstrair o banco de dados SQLite de forma robusta e segura.
* **Coroutines e Flow:** Para processamento assíncrono e programação reativa (observação contínua de dados).
* **ViewModel:** Gerenciamento de estado da interface com suporte ao ciclo de vida do Android.
* **Navigation Compose:** Gerenciamento de rotas e navegação entre as telas do aplicativo.

---

## Arquitetura e Componentes

A aplicação segue a arquitetura MVVM (Model-View-ViewModel) aliada aos princípios do *Clean Architecture* na camada de dados.

### `TarefaRepository` (Repositório)
O repositório tem a responsabilidade de ser a **única fonte de verdade** para os dados das tarefas. Ele abstrai a origem dos dados (no caso, o `TarefaDao` do Room) do resto da aplicação. O repositório expõe operações de suspensão (`suspend fun`) para inserção, atualização e exclusão, além de expor a lista de tarefas como um `Flow`, permitindo que qualquer alteração no banco de dados seja reativamente propagada.

### `TarefaViewModel`
A ViewModel atua como a ponte entre o Repositório e a Interface de Usuário (UI). Suas responsabilidades incluem:
1. Coletar o `Flow` do repositório e convertê-lo em um `StateFlow` (usando `stateIn`), mantendo o estado da lista de tarefas em memória de forma segura para a UI.
2. Fornecer funções (`inserir`, `atualizar`, `deletar`) que executam as operações de banco de dados dentro do `viewModelScope`, garantindo que rodem em uma thread assíncrona (background) sem travar a interface do usuário.

### `ListaTarefasScreen` (Tela de Listagem)
Esta tela é responsável por exibir os dados ao usuário.
* **Observação de Estado:** Ela consome a lista de tarefas da ViewModel utilizando `collectAsStateWithLifecycle()`. Isso garante que a UI seja recomposta automaticamente sempre que o banco de dados for alterado, respeitando o ciclo de vida da tela para economizar recursos.
* **Disparo de Ações:** Quando o usuário interage com a lista (ex: clica no checkbox para concluir uma tarefa ou no ícone de lixeira para deletar), a tela não altera o dado diretamente; ela dispara *callbacks* (eventos) que chamam os métodos da ViewModel, que por sua vez atualizam o banco de dados.

### `FormularioTarefaScreen` (Tela de Formulário)
Esta tela é reaproveitada tanto para **cadastrar** quanto para **editar** tarefas.
* **Diferenciação:** Ela descobre qual ação tomar através do parâmetro `tarefaId`. Se o `tarefaId` for `0` (zero), a tela entende que é uma criação e inicia com os campos de texto vazios. Se o `tarefaId` for diferente de `0`, ela busca a tarefa correspondente na lista mantida pela ViewModel e preenche os campos (`tituloInicial` e `descricaoInicial`). Ao clicar em "Salvar", ela verifica novamente o ID para decidir se chama o método `inserir` ou `atualizar` da ViewModel.

### `AppNavigation` (Navegação e Rotas)
Responsável por orquestrar a troca de telas através do `NavHost`. Foram configuradas duas rotas:
1. `"lista"`: Rota inicial que carrega a `ListaTarefasScreen`.
2. `"formulario/{tarefaId}"`: Rota que carrega o formulário. O `tarefaId` é um argumento dinâmico passado na URL de navegação.
* **Passagem de ID:** A navegação extrai esse argumento, faz o *parsing* seguro para um tipo Inteiro (`NavType.IntType`) e repassa para o formulário.

### `MainActivity`
É o ponto de entrada (entry point) do aplicativo. Sua responsabilidade foi reduzida apenas a:
1. **Criar a ViewModel:** Utiliza uma `Factory` personalizada para instanciar a `TarefaViewModel`, passando o contexto da aplicação para criar o banco de dados Room e injetar o Repositório.
2. **Iniciar a Navegação:** Chama o componente `AppNavigation` repassando a instância da ViewModel, garantindo que toda a árvore de telas do Compose compartilhe o mesmo estado.

---

## Como executar o projeto

1. Abra o projeto no **Android Studio**.
2. Aguarde a sincronização do Gradle (verifique se a barra inferior indica conclusão).
3. Caso necessário, vá em **Build > Rebuild Project** para compilar o KSP e as classes do Room.
4. Conecte um dispositivo físico via cabo USB ou inicie um Emulador (AVD).
5. Clique no botão verde de **Run 'app' (Play)** na barra superior do Android Studio.
6. *(Opcional)* Para rodar a suíte de testes de banco de dados, abra o arquivo `TarefaDaoTest.kt` na pasta de testes instrumentados e clique no ícone de play ao lado da declaração da classe.

---

## Evidências

*(Adicione aqui os prints gerados durante o desenvolvimento e execução da atividade)*

![Evidência 1 - Tela de Lista Vazia](![pagina inicial.png](docs/evidencias/pagina%20inicial.png))
![Evidência 2 - Inserindo nova Tarefa](![cadastrando nova tarefa.png](docs/evidencias/cadastrando%20nova%20tarefa.png))
![Evidência 3 - Tela de Lista com Tarefas](![lista com diferentes tarefas.png](docs/evidencias/lista%20com%20diferentes%20tarefas.png))
![Evidência 4 - Testes Instrumentados passando](![codigo pronto e rodando.png](docs/evidencias/codigo%20pronto%20e%20rodando.png))