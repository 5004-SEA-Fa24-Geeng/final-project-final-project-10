# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project. 

```mermaid


classDiagram
    %% Model Classes
    class Model.Pokemon {
        -int id
        -String name
        -List~String~ types
        -int level
        -int cp
        -List~Move~ moves
        -String rarity
        -int evolutionStage
        -String imageUrl
        +Model.Pokemon(id, name, types, level)
        +getTypes() List~String~
        +calculatePower() int
        +canEvolve() boolean
        +getTypeEffectiveness(type) double
        +addMove(move) void
        +removeMove(move) void
    }
    
    class Move {
        -String name
        -String type
        -int power
        -int accuracy
        +Move(name, type, power)
        +getEffectiveness(pokemonType) double
    }
    
    class PokemonCollection {
        -List~Model.Pokemon~ pokemons
        +addPokemon(pokemon) void
        +removePokemon(pokemon) void
        +getPokemonById(id) Model.Pokemon
        +searchByName(name) List~Model.Pokemon~
        +filterByType(type) List~Model.Pokemon~
        +sortBy(criteria) List~Model.Pokemon~
        +getStatistics() Map
    }
    
    class Team {
        -String name
        -String description
        -List~Model.Pokemon~ members
        -int maxSize
        +Team(name, description)
        +addPokemon(pokemon) boolean
        +removePokemon(pokemon) boolean
        +getTeamPower() int
        +analyzeWeaknesses() Map
        +getTypeDistribution() Map
    }
    
    %% Controller Classes
    class CollectionController {
        -PokemonCollection collection
        +addNewPokemon(data) Model.Pokemon
        +updatePokemon(id, data) boolean
        +deletePokemon(id) boolean
        +searchPokemons(criteria) List~Model.Pokemon~
        +getCollectionStatistics() Map
    }
    
    class TeamController {
        -List~Team~ teams
        +createTeam(name, description) Team
        +deleteTeam(teamId) boolean
        +addPokemonToTeam(teamId, pokemonId) boolean
        +removePokemonFromTeam(teamId, pokemonId) boolean
        +analyzeTeam(teamId) Map
    }
    
    class FileController {
        -FileManager fileManager
        +exportCollection(format, path) boolean
        +importCollection(format, path) PokemonCollection
        +exportTeam(teamId, format, path) boolean
        +importTeam(format, path) Team
    }
    
    %% View Classes
    class MainView {
        +initialize() void
        +showCollectionView() void
        +showTeamView() void
        +showDetailView(pokemonId) void
        +showSearchView() void
    }
    
    class CollectionView {
        -CollectionController controller
        +displayPokemons(pokemons) void
        +showAddPokemonDialog() void
        +showEditPokemonDialog(pokemon) void
        +refreshView() void
    }
    
    class TeamView {
        -TeamController controller
        +displayTeams(teams) void
        +displayTeamDetails(team) void
        +showCreateTeamDialog() void
        +refreshView() void
    }
    
    class PokemonDetailView {
        +displayPokemonDetails(pokemon) void
        +showMovesList(moves) void
        +showEvolutionInfo(pokemon) void
    }
    
    %% Utility Classes
    class FileManager {
        +exportToJSON(data, path) boolean
        +importFromJSON(path) Object
        +exportToCSV(data, path) boolean
        +importFromCSV(path) Object
        +exportToXML(data, path) boolean
        +importFromXML(path) Object
    }
    
    class InputValidator {
        +validatePokemonData(data) boolean
        +validateTeamComposition(team) boolean
        +getErrorMessages() List~String~
    }
    
    class DataAccessLayer {
        <<interface>>
        +getPokemonData(id) Map
        +getAllPokemonData() List~Map~
        +saveData(data) boolean
    }
    
    class LocalStorageDAO {
        -String storagePath
        +getPokemonData(id) Map
        +getAllPokemonData() List~Map~
        +saveData(data) boolean
    }
    
    class APIAccessDAO {
        -String apiUrl
        -String apiKey
        +getPokemonData(id) Map
        +getAllPokemonData() List~Map~
        +saveData(data) boolean
    }
    
    %% Relationships
    Model.Pokemon "1" *-- "0..*" Move : has
    PokemonCollection "1" *-- "0..*" Model.Pokemon : contains
    Team "1" o-- "0..*" Model.Pokemon : references
    
    CollectionController --> PokemonCollection : manages
    TeamController --> Team : manages
    FileController --> FileManager : uses
    
    CollectionView --> CollectionController : uses
    TeamView --> TeamController : uses
    PokemonDetailView --> Model.Pokemon : displays
    
    MainView --> CollectionView : contains
    MainView --> TeamView : contains
    MainView --> PokemonDetailView : contains
    
    FileController ..> InputValidator : uses
    CollectionController ..> InputValidator : uses
    TeamController ..> InputValidator : uses
    
    DataAccessLayer <|.. LocalStorageDAO : implements
    DataAccessLayer <|.. APIAccessDAO : implements
    CollectionController --> DataAccessLayer : uses

```