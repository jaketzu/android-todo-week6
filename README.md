## Mitä Room tekee?

Room on Googlen kehittämä abstraktiokerros SQLiten päälle, joka tekee tietokannan käytöstä helppoa ja tyyppiturvalista. Room mahdollistaa compile-time SQL-kyselyjen validoinnin, integraation LiveDatan ja Flow:n kanssa ja type-safe Kotlin API:n.  


## Projektin rakenne

Projektin rakenne on aika samanlainen kuin opettajan nettisivulla.

```
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── TaskDao.kt           ← DAO (tietokantakyselyt)
│   │   ├── entity/
│   │   │   └── Task.kt              ← Entity (taulun rakenne)
│   │   └── AppDatabase.kt           ← Database (singleton-yhteys)
│   │   └── TasksUIState.kt          ← Käyttöliittymän muuttujien dataluokka, käytössä ViewModelissa
│   └── repository/
│       └── TaskRepository.kt        ← Repository (abstraktiokerros)
├── navigation/
│   └── Routes.kt                    ← Navigaattorin navigaatiosijainnit vakioarvoina
├── ui/
│   ├── components/
│   │   └── ErrorDialog.kt           ← Virhedialogi
│   │   └── TaskCard.kt              ← Yksittäinen tehtäväkortti
│   │   └── TaskDialog.kt            ← Lisäys- ja muokkausdialogi
│   ├── screens/
│   │   └── HomeScreen.kt            ← Päänäkymä
│   │   └── CalendarScreen.kt        ← Kalenterinäkymä
│   │   └── SettingsScreen.kt        ← Asetukset
├── viewmodel/
│   └── TaskViewModel.kt             ← ViewModel (logiikka)
└── MainActivity.kt                  ← Sovelluksen käynnistys
```


## Miten datavirta kulkee?

UI kutsuu ViewModelista komennon, joka kutsuu repositoryn komennon, joka kutsuu Room-kirjaston DAO:n kautta SQL-kyselyn. 

### [Demo video](https://oulu.cloud.panopto.eu/Panopto/Pages/Viewer.aspx?id=3cf3f92e-4528-4ad4-b3e5-b3f9012dcc74)
