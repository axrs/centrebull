# Centrebull 

[![CircleCI](https://circleci.com/gh/axrs/centrebull/tree/master.svg?style=shield)](https://circleci.com/gh/axrs/centrebull/tree/master)

[![CircleCI](https://circleci.com/gh/axrs/centrebull/tree/develop.svg?style=shield)](https://circleci.com/gh/axrs/centrebull/tree/develop)

* [Dependencies](#dependencies)
* [Setup](#setup)
* [Development](#development)
  * [Server](#server)
  * [Figwheel](#figwheel)
  * [LESS](#less)
* [Unit Tests](#unit-tests)  
* [Project Structure and files](#project-structure-and-files)
* [License](#license)

## Dependencies

* [Leiningen][1] 2.0 or above for everything build related
* [VirtualBox][2] and [Vagrant][3] for virtualising the [PostgreSQL][4] database
* [LessCSS][6] is used to help write the application specific CSS
* [Reagent][7] and [Re-Frame][8] are used as the front end UI framework
* [Figwheel][11] is used to provide hot reloading to the web browser while coding.
* A modified variant of [BareCSS][10] is used for all visual and aesthetics

The project was built from the *[Luminus][5]* template. It's recommended
reading through some of the documentation before diving in. The [Re-Frame][9]
documentation is extremely comprehensive and will provide insight into how the
UI views are rendered to the end user.

[1]: https://github.com/technomancy/leiningen
[2]: https://www.virtualbox.org/
[3]: https://www.vagrantup.com/
[4]: https://www.postgresql.org/
[5]: http://www.luminusweb.net/
[6]: http://lesscss.org/
[7]: https://github.com/reagent-project/reagent
[8]: https://github.com/Day8/re-frame
[9]: https://github.com/Day8/re-frame/tree/master/docs
[10]: http://barecss.com/
[11]: https://github.com/bhauman/lein-figwheel
[12]: http://localhost:3000/

## Setup

Configure and run the Vagrant box which hosts the PostgreSQL database-url and
migrate the database to the latest revision.

```bash
vagrant up
./centrebull.sh clean
```

## Development

There are three [Leiningen][1] commands that are used to assist the developer.
Each command watches for changes and reloads components accordingly.

### Server

The **Centrebull** web server can be started by issuing the following command in
a new terminal instance.

```bash
lein run
```

Once running, open your web browser to [http://localhost:3000/][12] to start
using the application.

### Figwheel

> Figwheel builds your ClojureScript code and hot loads it into the browser as you are coding!

It is a core tool in the `ClojureScript` toolkit and provides the powerful
functionality to see the web browser reload changes without modifying state.
Meaning any changes made, paths taken, etc. are still applied. This is not your
typical browser reload.

```bash
lein figwheel
```

No other changes are required. Point your web browser to
[http://localhost:3000/][12] to start using the application with auto-refreshing.

### LESS

To watch for `.less` file changes and trigger a recompilation, run the following
command in a new terminal instance:

```bash
lein less auto
```

Alternatively, issue the following command to compile the `.less` into `.css`
once:

```bash
lein less once
```

#### Node.js Alternative

Leiningen can be a resource intensive service, especially if all it needs to do is
watch and compile some LESS into CSS.  As a low resource alternative, Node.js and
Gulp can be used.

```bash
npm install #To download the latest node.js dependencies
gulp #To run the less compiler and watch for changes
```

## Unit Tests

Unit tests are placed in the `centrebull.test.*` namespace and try to be as
inclusive as possible. To date, the tests only cover the server and validator
integrations.

To test everything is okay, issue the following in a terminal instance:

```bash
lein test
```

## Project Structure and files

```
centrebull.sh                           //Bash helper utilities for centrebull
project.clj                             //Project and plugin configuration. Also required dependencies.

Vagrantfile                             //Vagrant file that will configure and launch a VM to host the Postgres Database
                                        //Used for local development only.

Capstanfile                             //Capstain configuration for running the application in a complete virtual 
                                        //machine image that will run on any hypervisor with OSv support.

Dockerfile                              //Docker configuration file to allow docker to build and deploy an image for
                                        //hosting the application.
build
└── provision.sh                        //Bash script used by Vagrant to install and configure Postgres inside the VM.  

resources
├── docs                                 
│   └── docs.md                         //Doc created by Luminus and currently rendered as the app homepage.
│
├── migrations                                 
│   └── <migration>.up.sql              //SQL commands necessary to migrate the database to the next revision.
│   └── <migration>.down.sql            //SQL commands necessary to roll back a migration. Note: Note used for this project.
│
├── public                                 
│   └── <file>                          //Publicly served resources and files (like a typical web server)
│
├── sql                                 
│   └── <hug-sql>.sql                   //SQL commands to manipulate data. HugSQL flavour.
│
└── templates                                 
    └── <file>.html                     //HTML Templates used by the server to do any basic server-side rendering.
    
src
├── clj                                 //---- Clojure compiled to Java ----
│   └── centrebull
│       ├── config.clj                  //Defines configuration initialisation.             
│       ├── core.clj                    //Defines how the server starts and bootstraps itself.
│       ├── handler.clj                 //Defines the actual serverside application and initialises any routes.
│       ├── layout.clj                  //Server side rendering to provide the initial index and error pages.           
│       ├── middleware.clj              //Request and response middleware and injections.               
│       ├── auth   
│       │   └── middleware.clj          //Contains middleware to handle authentication of requests.
│       ├── db                  
│       │   └── core.clj                //Database connection and state management to Postgres.
│       ├-- encryption          
│       │   └── core.clj                //All things hashing and encrypting for the project.
│       ├-- routes          
│       │   └── core.clj                //All end point definitions and their respective action injection.       
│       └-- <resouce>          
│           └── util.clj                //Utility functions relating to the resource.
│              └── actions
│                  ├── <action>.clj    //API End point action and processing.
│                  └── <action>.clj    //Each file contains the logic processes an entire endpoint action.
│    
├── cljc                                //---- Clojure compiled to both Java and Javascript (shared domain) ----
│   └── centrebull
│       ├── util.cljc                   //Shared utility functions. 
│       └-- <resouce>          
│           └── validators.cljc         //Resource specific input/body validators (i.e form validation before POSTing).
│    
├── cljs                                //---- Clojure compiled Javascript ----
│   └── centrebull
│       ├── ajax.cljs                   //AJAX request functions and interceptors.
│       ├── core.cljs                   //Core of the re-frame application bootstrap and configuration.
│       ├── db.cljs                     //Initialisation of the in-memory database.
│       ├── handlers.cljs               //Core application event handling.
│       ├── subscriptions.ljs           //Core application data subscriptions.
│       ├── actions 
│       │   ├── core.cljs               //Single point of injection and loading for other actions.
│       │   └── <resource>.cljs         //Resource specific events and subscriptions.
│       ├-- <resouce>          
│       │   ├── routes.cljs             //Routing definitions for re-frame pages and basic logic required for the pages.
│       │   ├── subscriptions.cljs      //Any subscriptions required by the enclosed pages.
│       │   ├── views.cljs              //Hiccup annotated views defining the resource pages.
│       │   └ <sub-resource>          
│       │       ├── routes.cljs         //Resource sub pages (if required). i.e. Resource edit pages.
│       │       ├── subscriptions.cljs      
│       │       └── views.cljs          
│       └-- components       
│           └── <component>.cljs        //Common Hiccup structures and components for pages .
│
├── less                                //---- LESS (compiled to CSS) ----
│   ├── _<element>.less                 //CSS Styling for the target elements.
│   ├-- _variables.less                 //Common variables for use with other LESS files (fonts, colours, etc).
│   └-- bare.less                       //LESS file that gets compiled and includes all other styles.
│
└── icons                               //---- Original SVG Icons (manually exported as xml) into LESS files ----

test
└── clj                                 //---- Unit tests for the server and validators ----
    └── centrebull
        └-- <resouce>         
            └── <test>.clj              //Unit tests for the resource namespace

env
└── <env>                               //Environment specific configurations and additions for running the application  
```

## License

Copyright © 2016+ AXRS
