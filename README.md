# Slideshow Service

## Build and assembly docker image

- For build project execute maven command:

`mvn clean install`

- For build project with docker image execute next command:

`mvn clean install -Denv=docker`

## Test coverage report

In the project added the jacoco maven plugin for perspective test coverage.
The report is `target/jacoco-output/index.html`

## Run service in docker compose

The service running by docker compose tool

`docker compose up`

## API Description

### Images API


- Upload new file to the service `localhost:8082/addImage` <br/> 
`curl --location 'localhost:8082/addImage' \
--form 'file=@"path_to_file/starwars_2.png"'`<br/>
The result is image URL. For example `/images/1`
<br/>
- Getting image by ID execute request `curl --location 'localhost:8082/images/1' \
--data ''`
- Search image example request `curl --location 'http://localhost:8082/images/search?keyword=starwars'`

### Slideshow API

- Create new slideshow example request <br/>
`curl --location 'http://localhost:8082/addSlideshow' \
--header 'Content-Type: application/json' \
--data '{
  "name": "star wars",
  "description": "test slide show",
  "slides": [
    {"imageId": 1,
      "duration":  5,
      "position": 1
    },
    {"imageId": 2,
      "duration":  5,
      "position": 2
    },
    {"imageId": 4,
      "duration":  5,
      "position": 3
    },
    {"imageId": 5,
      "duration":  5,
      "position": 3
    }
  ]
}'`

- Get slideshow order <br/>
`curl --location 'localhost:8082/slideshow/1/slideshowOrder'`
- Proof of play API execute request: <br/>
`curl --location --request POST 'localhost:8082/slideshow/1/proof-of-play/7'`





