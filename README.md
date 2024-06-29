# Projeto Tute - Manutenção de Software

## Sobre o Repositório
Este repositório contém o projeto Tute, desenvolvido como parte da disciplina de Manutenção de Software. A segunda iteração deste projeto tem como objetivo realizar manutenção em repositórios descontinuados, aplicando leis e conceitos estudados ao longo do curso.

## O Jogo TUTE
O Tute é um jogo de cartas muito popular na Espanha. Existem várias versões desse jogo, todas com as mesmas regras básicas e jogabilidade. Neste jogo, os jogadores se unem em equipes, ganham pontos, vencem truques e tentam acumular a maior pontuação possível.

### Como Jogar

> 1. Objetivo do Jogo:
>> - O objetivo do Tute é ganhar a maior quantidade de pontos possível.
> 2. Número de Jogadores:
>> - O jogo é jogado com quatro jogadores.
> 3. Materiais:
>> - Um baralho padrão espanhol com 40 cartas.
> 4. Preparação:
>> - O distribuidor é escolhido aleatoriamente.
>> - O distribuidor embaralha as cartas e o jogador à sua esquerda corta o baralho.
>> - Cada jogador recebe dez cartas, começando pelo jogador à direita do distribuidor.
>> - A última carta distribuída determina o naipe de trunfo para a mão.
> 5. Jogabilidade:
>> - As cartas são jogadas em truques, e o objetivo é ganhar os truques que contêm as cartas de maior pontuação.
>> - O jogo continua no sentido anti-horário.
>> - O jogador à direita do distribuidor lidera o primeiro truque.
>> - Qualquer carta pode ser a carta líder, e os outros jogadores devem tentar seguir o naipe se puderem.
>> - Se não puderem seguir o naipe e ninguém jogou um trunfo, eles devem jogar um trunfo.
>> - Se nenhum trunfo for jogado, o truque é vencido pela carta mais alta do naipe liderado.
>> - Os jogadores podem marcar pontos adicionais declarando quando têm o rei e o cavalo do mesmo naipe. O rei e o cavalo de um naipe que não é o naipe de trunfo valem vinte pontos. O rei e o cavalo do naipe de trunfo valem quarenta pontos.
>> - Os jogadores devem vencer um truque para poderem “cantar”. Mesmo que tenham a combinação na mão, não podem cantar até depois de vencerem um truque.
> 6. Fim do Jogo:
>> - O jogo termina quando todas as cartas foram jogadas.
>> - As equipes somam todos os pontos dos truques que ganharam.
>> - A última equipe a vencer um truque ganha dez pontos adicionais.
>> - **A equipe com mais pontos vence o jogo.**

## Tecnologias Utilizadas
- Java JDK 18

## Estrutura do Projeto
```text
📦MS29S-Tute-UTFPR
 ┣ 📂bin -> Arquivos binários
 ┣ 📂classes -> Arquivos compilados
 ┣ 📂src -> Código fonte
 ┃ ┣ 📂iams -> Pacote principal
 ┃ ┃ ┣ 📂cardgame -> Código Lógico do Jogo
 ┃ ┃ ┃ ┣ 📂animators -> Classes relacionadas à animação
 ┃ ┃ ┃ ┃ ┣ 📜AnimationController.java
 ┃ ┃ ┃ ┃ ┣ 📜MoveCardAnimator.java
 ┃ ┃ ┃ ┃ ┣ 📜MoveToBackAnimator.java
 ┃ ┃ ┃ ┃ ┣ 📜MoveToFrontAnimator.java
 ┃ ┃ ┃ ┃ ┣ 📜MultiAnimator.java
 ┃ ┃ ┃ ┃ ┣ 📜ReverseAnimator.java
 ┃ ┃ ┃ ┃ ┣ 📜ThrowCardAnimator.java
 ┃ ┃ ┃ ┃ ┗ 📜WaitAnimator.java
 ┃ ┃ ┃ ┣ 📂tute -> Definição do jogo Tute
 ┃ ┃ ┃ ┃ ┣ 📂ai -> Classes relacionadas à inteligência artificial
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AITester.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BasicTuteAI.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜SmartTuteAI.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TuteAI.java
 ┃ ┃ ┃ ┃ ┣ 📂movement -> Classes relacionadas aos movimentos do jogo
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Movement.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PintaMovement.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ThrowMovement.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ThrowResponseMovement.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜TuteMovement.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TwentyFortyMovement.java
 ┃ ┃ ┃ ┃ ┣ 📂tr -> Classes relacionadas a tradução
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Normas.txt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Regras.txt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Rules.txt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Translator.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜TranslatorEn.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜TranslatorEs.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TranslatorPtBr.java
 ┃ ┃ ┃ ┃ ┣ 📜Card.java
 ┃ ┃ ┃ ┃ ┣ 📜CardModel.java
 ┃ ┃ ┃ ┃ ┣ 📜GameMouseListener.java
 ┃ ┃ ┃ ┃ ┣ 📜HumanPlayer.java
 ┃ ┃ ┃ ┃ ┣ 📜Languages.java
 ┃ ┃ ┃ ┃ ┣ 📜Main.java -> Classe principal
 ┃ ┃ ┃ ┃ ┣ 📜PlayTurn.java
 ┃ ┃ ┃ ┃ ┣ 📜RulesController.java
 ┃ ┃ ┃ ┃ ┣ 📜spanish-deck.png
 ┃ ┃ ┃ ┃ ┣ 📜TuteController.java
 ┃ ┃ ┃ ┃ ┣ 📜TuteGame.java
 ┃ ┃ ┃ ┃ ┣ 📜TuteGamePlayerContext.java
 ┃ ┃ ┃ ┃ ┗ 📜TuteGameUI.java
 ┃ ┃ ┃ ┣ 📜icon.png
 ┃ ┃ ┃ ┗ 📜iconhq.png
 ┃ ┃ ┗ 📂ui -> Recursos da interface do usuário
 ┃ ┃   ┗ 📜GraphicsPanel.java
 ┗ 📜README.md
```

## Contribuidores
- Daniel Fonseca da Silva
- Davi Gil Brito Vaz Takayama
- João Pedro dos Santos Batista
- Paulo Moschen

## Melhorias Conduzidas
- Adicionada documentação geral ao repositório do projeto;
- Corrigido a ausência de limite mínimo ao tamanho da janela quando não maximizado, e adicionada expansão proporcional da janela ao redimensionar;
- Mudar a cor do plano de fundo não reinicia mais o jogo;
- Implementado botão que exibe a hierarquia das cartas em meio à partida;
- Removido arquivos da versão JavaScript do projeto e demais arquivos gerados altomaticamente, assim como código morto.

## Possíveis melhorias futuras
- Refatoração semântica do código para se adaptar a definição da linguagem Java;
- Melhoria visual no botão de exibir hierarquia das cartas;
- Traduzir conteúdo do menu de hierarquia para os demais idiomas;
- Ajustar menu de hierarquia para diferentes tamanhos de janela.

## Licença
Este projeto está licenciado sob a Licença MIT.

## Contato
Para mais informações, entre em contato com os contribuidores através do GitHub.
