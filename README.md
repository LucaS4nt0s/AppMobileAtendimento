# 📱 AppMobileAtendimento - Gestão e Registro de Demandas de Suporte

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Java](https://img.shields.io/badge/Language-Java%2011-orange.svg)
![BaaS](https://img.shields.io/badge/Backend-Back4App%20%7C%20Parse%20SDK-blue.svg)
![UI](https://img.shields.io/badge/UI-Material%20Design-brightgreen.svg)

## 📌 Visão Geral
O **AppMobileAtendimento** é uma aplicação móvel nativa desenvolvida em **Java** para o ecossistema Android, projetada para gerenciar e otimizar o fluxo de atendimento a chamados de assistência e solicitações de serviços em campo.

A solução atua na ponta de suporte operacional, permitindo que operadores e técnicos cadastrem ocorrências com evidências fotográficas, filtrem e gerenciem ordens de serviço por prioridade/status e acompanhem métricas consolidadas em tempo real. Os dados são sincronizados na nuvem utilizando **Back4App** (Parse Platform BaaS).

---

## 🚀 Funcionalidades Principais
- 📝 **Abertura e Cadastro de Chamados**: Formulário completo com descrição da demanda, categoria, nível de prioridade e dados do solicitante.
- 📸 **Captura e Anexo de Fotos**: Integração com a câmera nativa do dispositivo através de `FileProvider` e `MediaStore`, permitindo comprovação visual das demandas.
- 🔄 **Ciclo de Vida do Atendimento**: Workflow para alteração dinâmica de status (`Pendente`, `Em Atendimento`, `Concluído`, `Cancelado`).
- 🔍 **Filtros e Busca Avançada**: Consulta segmentada de chamados por urgência, técnico responsável ou período.
- 📊 **Painel de Estatísticas e Métricas**: Visualização consolidada de indicadores de desempenho (total de chamados abertos, atendidos e tempo médio).
- 🧭 **Navegação Ergonômica**: Menu lateral moderno com `DrawerLayout`, `NavigationView` e estilização baseada no Material Design 3.

---

## 🛠️ Tecnologias e Ferramentas
- **Linguagem**: Java (JDK 11)
- **Plataforma**: Android SDK (Min SDK: 24 / Target SDK: 36)
- **Interface e Componentes**:
  - `Material Components for Android` (Chips, Toolbars, Floating Action Buttons)
  - `RecyclerView` & `CardView` com ViewHolder pattern para renderização performática de listas
  - `DrawerLayout` & `ConstraintLayout`
- **Backend as a Service (BaaS)**: Parse Android SDK (`com.github.parse-community:Parse-SDK-Android:4.4.0`) integrado ao cluster Back4App.
- **Build System**: Gradle com suporte a Kotlin DSL (`build.gradle.kts`).

---

## 🏛️ Arquitetura e Boas Práticas
- **Padrão ViewHolder & Adapters Customizados**: Desacoplamento entre a camada de dados e a interface visual com reciclagem eficiente de views.
- **Persistência Assíncrona via Callbacks**: Comunicação não-bloqueante com o backend (`Back4AppHelper.java`) utilizando queries assíncronas do Parse Framework (`ParseQuery.findInBackground`, `saveInBackground`), mantendo a UI thread sempre responsiva.
- **Segurança e Permissões**: Tratamento de permissões em tempo de execução para captura de fotos (`Manifest.permission.CAMERA`, permissões de armazenamento e `FileProvider` seguro).

---

## 📂 Estrutura do Projeto
```plaintext
app/src/main/
├── AndroidManifest.xml                  # Permissões, Activities e configurações do app
├── java/com/example/trabalhopratico1/
│   ├── MainActivity.java                # Dashboard principal e navegação do menu
│   ├── CadastrarDemanda.java            # Fluxo de criação de chamados com captura de câmera
│   ├── ListarChamados.java              # Listagem em tempo real com RecyclerView
│   ├── FiltrarChamados.java             # Mecanismo de filtros dinâmicos
│   ├── Atendimento.java                 # Gestão de status e encerramento de ocorrências
│   ├── Estatisticas.java                # Resumo quantitativo e relatórios
│   ├── Back4AppHelper.java              # Wrapper e abstração de chamadas BaaS (Parse SDK)
│   ├── Adaptador.java                   # Adapter customizado para itens da lista
│   └── RecyHolder.java                  # ViewHolder pattern para componentes gráficos
└── res/
    ├── layout/                          # Layouts XML responsivos (ConstraintLayout / Drawer)
    ├── menu/                            # Menus da NavigationDrawer
    └── values/                          # Paleta de cores, temas escuro/claro e strings
```

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
- **Android Studio** (versão Ladybug / Iguana ou superior).
- **JDK 11** instalado e configurado.
- Dispositivo Android físico com depuração USB ativada ou emulador Android (API 24+).
- Chaves de acesso ao Back4App (Application ID e Client Key) configuradas em `App.java`.

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone https://github.com/LucaS4nt0s/AppMobileAtendimento.git
   ```
2. Abra o projeto no **Android Studio**.
3. Aguarde a sincronização dos arquivos Gradle e resolução de dependências.
4. Conecte o emulador ou aparelho físico.
5. Clique no botão **Run (Shift + F10)** para compilar e inicializar o aplicativo.

---

## 👨‍💻 Autor
Desenvolvido por **Luca Samuel dos Santos** ([@LucaS4nt0s](https://github.com/LucaS4nt0s)).
