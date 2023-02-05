# Coronavac Framework

An amazing homemade Spring-like dependency injection framework.

### Framework Structure

```
├───coronavac-commons
│   ├───src
│   │   └───main
│   │       └───java
│   │           └───com
│   │               └───da0hn
│   │                   └───coronavac
│   │                       └───commons
│   │                           └───exceptions
├───coronavac-core
│   ├───src
│   │   └───main
│   │       └───java
│   │           └───com
│   │               └───da0hn
│   │                   └───coronavac
│   │                       └───core
│   │                           └───annotations
└───sample (Project representing a simple usage of framework)
    ├───src
    │   └───main
    │       └───java
    │           └───com
    │               └───da0hn
    │                   └───coronavac
    │                       └───sample
    │                           └───config
```

### Coronavac vs Spring - CDI Framework

|        Coronavac Framework         |     Spring Framework     |
|:----------------------------------:|:------------------------:|
|      `@CoronavacApplication`       | `@SpringBootApplication` |
|               `@Get`               |       `@Autowired`       |
|            `@Component`            |       `@Component`       |
|           `@Repository`            |      `@Repository`       |
| `@Configuration` (not implemented) |     `@Configuration`     | 
|  `@ScanPackage` (not implemented)  |     `@ComponentScan`     | 
| `@ScanPackages` (not implemented)  |    `@ComponentScans`     | 


