<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
# Lantern

[![Build Status](https://travis-ci.com/BalajiDany/lantern-server.svg?token=pqdDDYcKAzzpYPLgX2BL&branch=main)](https://travis-ci.com/BalajiDany/lantern-server)
[![GitHub contributors](https://img.shields.io/github/contributors/BalajiDany/lantern-server.svg)](https://github.com/BalajiDany/lantern-server/graphs/contributors)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/BalajiDany/lantern-server/blob/main/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/BalajiDany/lantern-server.svg)](https://github.com/BalajiDany/lantern-server/issues)
[![GitHub forks](https://img.shields.io/github/forks/BalajiDany/lantern-server.svg)](https://github.com/BalajiDany/lantern-server/network)
[![GitHub stars](https://img.shields.io/github/stars/BalajiDany/lantern-server.svg)](https://github.com/BalajiDany/lantern-server/stargazers)

<!-- ABOUT THE PROJECT -->
### A Meta Search Engine

Welcome :wave:, Yet another java based Meta Search Engine with a highly modular structure. It rapidly sends queries to several search engines, aggregates the results, and categorizes the result by search engines they come from.

Here's why:
* **Highly modular** - Adding a new search engine/ Updating the previous engine is easy and *Highly Configurable.*
* **Privacy respecting** - Focused mainly on protecting the privacy of a user.
* **More information** - Get much more information than you using a standard engine :upside_down_face:

you can also deploy, and create your personal instance. We highly recommend not to share the instance. Most of the search engines will block the IP if it identifies unusual traffic.
To know more about MetaSearch Engine refer [ryte-wiki](https://en.ryte.com/wiki/Meta_Search_Engine)

A list of commonly used resources that I find helpful are listed in the [acknowledgments](https://github.com/BalajiDany/lantern-server#acknowledgements).

### Instance
For Demo purpose, we deployed this in heroku app : [lantern-engine](https://lantern-engine.herokuapp.com/lantern/search).
<br>As of now we used free plan/tier (since ONLY for **DEMO PURPOSE**).
<br>**For Usage please create your own personal instance.**


### Built With

List of the major library that is used in these projects. 
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Lombok](https://projectlombok.org/)
* [HttpClient](https://hc.apache.org/httpcomponents-client-ga/)
* [jsoup](https://jsoup.org/)
* [json](http://www.JSON.org/)

<!-- GETTING STARTED -->
## Getting Started

Currently, We support Web Application developed in angular, To Add/Change Function in WebApplication please refer [front-repo](https://github.com/BalajiDany/lantern-web-client.git)

For Installation and Development (back-end), please follow the below steps.

### Prerequisites

For building and running the application you need:

* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org)
* [Git](https://git-scm.com/downloads)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/BalajiDany/lantern-server.git
   ```
2. Navigate to the folder
   ```sh
   cd lantern-server
   ```
3. Start the application using maven
   ```sh
   # For Mac
   chmod +x mvnw
   ./mvnw spring-boot:run
   
   # For Windows: 
   mvnw spring-boot:run
   ```
   * Navigate to http://localhost:8080/lantern

<!-- USAGE EXAMPLES -->
## Available Search Engines

- General :cookie:
  - [Bing](https://www.bing.com/)
  - [DuckDuckGo](https://duckduckgo.com/)
  - [Google](https://www.google.com/)
  - [Yahoo](https://yahoo.com/)
- Torrent :beer:
  - [kickass](/)
  - [LimeTorrent](/)
  - [PirateBay](/)
  - [Torrent Finder](/) (PirateBay Proxy)
- Video :popcorn:
  - [Bing Video](https://www.bing.com/videos)
  - [Google Video](https://www.google.com/videohp)
- Code :pizza:
  - [Stack Overflow (Powered by Google)](https://stackoverflow.com/)

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->
## License

Apache-2.0 License - Please have a look at the [LICENSE](LICENSE) for more details.


<!-- CONTACT -->
## Contact

Balaji Dhanapal - d.balaji.mc@gmail.com

Pavithra Palanisamy - pavithrakowsika@gmail.com

Project Link: [https://github.com/BalajiDany/lantern-server](https://github.com/BalajiDany/lantern-server)

<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
* [Img Shields](https://shields.io)
* [Choose an Open Source License](https://choosealicense.com)
* [GitHub Pages](https://pages.github.com)

##

[Back To Top](#a-meta-search-engine)

