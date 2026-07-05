# ⚛️ Átomo — Simulador Quântico 3D

Simulação 3D interativa de átomos construída com [libGDX](https://libgdx.com/), que renderiza núcleo e nuvem eletrônica com base em **funções de probabilidade dos orbitais atômicos** (modelo mecânico-quântico simplificado), além de persistir os dados dos átomos em banco de dados via JPA.

Projeto gerado originalmente com [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

---

## ✨ Funcionalidades

- **Renderização 3D em tempo real** do núcleo (prótons/nêutrons) e da nuvem eletrônica.
- **Modelo probabilístico de orbitais**, com suporte aos orbitais `s1`, `s2`, `s3`, `p2x`, `p2y`, `p2z`, `p3x`, `p3y`, `p3z` — os elétrons são posicionados de acordo com a densidade de probabilidade $|\psi|^2$ de cada orbital.
- **Alternância automática de orbitais** ao longo do tempo, mostrando a evolução da nuvem eletrônica.
- **Múltiplos átomos na cena**, com navegação entre eles pelo teclado.
- **Persistência em banco de dados** via JPA/Hibernate, com fallback automático:
  - Tenta conectar a um banco **MySQL**;
  - Caso falhe, recorre a um banco **SQLite local** (`Banco.db3`).
- **HUD com informações da simulação** (número de átomos, orbital atual etc.), renderizado com fontes TrueType via FreeType.

---

## 🎮 Controles

| Tecla | Ação |
|---|---|
| `←` / `→` | Alterna entre os átomos da cena |
| Mouse (arrastar) | Rotaciona a câmera |
---

## 🧩 Arquitetura

O domínio da simulação é modelado em torno das seguintes classes (todas mapeadas como entidades JPA):

```
Atomo
 ├── Nucleo               → conjunto de Partículas (prótons/nêutrons)
 │    └── Particula
 └── NuvemEletronica      → conjunto de Elétrons
      └── Eletron
           └── Particula
```

- **`Particula`** — unidade básica de renderização (esfera 3D via `ModelBuilder`), representando prótons, nêutrons ou elétrons. Controla posição, carga, massa, cor e tamanho.
- **`Nucleo`** — agrega as partículas do núcleo, calcula o ponto médio (centro de massa) e o raio efetivo do núcleo.
- **`Eletron`** — encapsula uma `Particula` do tipo elétron, com nível de energia (que define sua cor) e tamanho variável.
- **`NuvemEletronica`** — o coração do modelo quântico: calcula a densidade de probabilidade de cada orbital via **amostragem por rejeição** (*rejection sampling*) e reposiciona os elétrons a cada quadro, simulando a nuvem eletrônica.
- **`Atomo`** — agrega `Nucleo` + `NuvemEletronica`, representando o átomo completo.
- **`Simulation`** — classe principal (`ApplicationAdapter`), responsável por inicializar a cena, câmera, iluminação, banco de dados e pelo laço de renderização.

---

## 🗄️ Persistência

O projeto utiliza **Jakarta Persistence (JPA)** com Hibernate. Ao iniciar, a aplicação tenta se conectar nesta ordem:

1. `PU_MySQL` — banco MySQL configurado no `persistence.xml`;
2. `PU_SQLite` — banco SQLite local, usado como fallback caso o MySQL não esteja disponível.

Os átomos gerados na cena são persistidos de forma assíncrona em uma thread separada, evitando bloquear o loop de renderização.

---

## 📁 Plataformas

- **`core`** — módulo principal com a lógica da aplicação, compartilhada entre todas as plataformas.
- **`lwjgl3`** — plataforma desktop primária, usando LWJGL3 (chamada de `desktop` em documentações mais antigas).

---

## 🛠️ Gradle

Este projeto usa [Gradle](https://gradle.org/) para gerenciar as dependências. O Gradle Wrapper já está incluído — use `gradlew.bat` (Windows) ou `./gradlew` (Linux/macOS).

### Comandos úteis

| Comando | Descrição |
|---|---|
| `./gradlew lwjgl3:run` | Executa a aplicação |
| `./gradlew build` | Compila e empacota o projeto |
| `./gradlew lwjgl3:jar` | Gera o `.jar` executável (em `lwjgl3/build/libs`) |
| `./gradlew clean` | Remove as pastas `build` |
| `./gradlew test` | Executa os testes (se houver) |
| `./gradlew idea` | Gera arquivos de projeto para o IntelliJ |
| `./gradlew eclipse` | Gera arquivos de projeto para o Eclipse |

### Flags úteis

| Flag | Descrição |
|---|---|
| `--continue` | Continua executando outras tarefas mesmo se uma falhar |
| `--daemon` | Usa o Gradle Daemon para acelerar execuções |
| `--offline` | Usa apenas dependências já armazenadas em cache |
| `--refresh-dependencies` | Força a revalidação de todas as dependências |

> 💡 Tarefas específicas de um módulo podem ser executadas com o prefixo `nome:`, por exemplo: `core:clean`.

---

## 🚀 Como executar

```bash
./gradlew lwjgl3:run
```

