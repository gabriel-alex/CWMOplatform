# Creative Workshop Management Ontology Platform

This platform is an experimental web application based on java.
The objective of this application is to develop functionalities based on [CWMO ontology](https://github.com/gabriel-alex/cwmo) in order to improve the management of creative workshop. The ambition of this platform is to provide the material to evaluate the usage of technology and notably AI-based technology in the management of the creative workshop. The work is based on the research done in the [Alex Gabriel's PhD thesis](https://www.researchgate.net/publication/313037722_Gestion_des_connaissances_lors_d%27un_processus_collaboratif_de_creativite).

The platform can be tested [here](https://cwmo-platform.herokuapp.com). Actually it is only a work in progress. It is uploaded to share the progress but design and functionalities could be broken. 

## Technical aspect
This application use the OWLAPI 5 and the HermiT inference engine.
Further documentation about OWLAPI can be found here:
- [OWLAPI github site](http://owlcs.github.io/owlapi/)
- [Old website of OWLAPI](http://owlapi.sourceforge.net/)
- [Valuable course about OWLAPI from Sean Bechhofer & Uli Sattler](http://syllabus.cs.manchester.ac.uk/pgt/2017/COMP62342/)

Concerning the management of the dependencies of this project, everything is declared in the pom.xml since it is a Maven project.

## Experiment the application on Heroku

This application is based on the example provided by Heroku.
To get more hint about how use Heroku, see the [Getting Started with Java on Heroku](https://devcenter.heroku.com/articles/getting-started-with-java) article.
For more information about using Java on Heroku, see the Dev Center article about [Java on Heroku](https://devcenter.heroku.com/categories/java)

Otherwise, if you are already familiar with Heroku, deploying it on your own sever is as simple as clicking on this button.  

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)
