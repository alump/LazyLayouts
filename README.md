# LazyLayouts Add-on for Vaadin 7

LazyLayout is an UI component add-on for Vaadin 7. It offers layouts that will provide on-demand content callbacks to
application logic. This allows to optimize DB queries to only provide handful of responses needed to view page full of
results.

## Build status

[![Build Status](http://siika.fi:8888/jenkins/job/LazyLayouts%20(Vaadin)/badge/icon)](http://siika.fi:8888/jenkins/job/LazyLayouts%20(Vaadin)/)

## Online demo

Try the add-on demo at http://siika.fi:8080/LazyLayoutsDemo

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to
http://vaadin.com/addon/lazylayouts

## Building and running demo

git clone https://github.com/alump/LazyLayouts.git
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for lazylayouts-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your lazylayouts-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the lazylayouts-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/lazylayouts-demo/ to see the application.

### Debugging client-side

The most common way of debugging and making changes to the client-side code is dev-mode. To create debug configuration for it, open lazylayouts-demo project properties and click "Create Development Mode Launch" button on the Vaadin tab. Right-click newly added "GWT development mode for lazylayouts-demo.launch" and choose Debug As > Debug Configurations... Open up Classpath tab for the development mode configuration and choose User Entries. Click Advanced... and select Add Folders. Choose Java and Resources under lazylayouts/src/main and click ok. Now you are ready to start debugging the client-side code by clicking debug. Click Launch Default Browser button in the GWT Development Mode in the launched application. Now you can modify and breakpoints to client-side classes and see changes by reloading the web page. 

Another way of debugging client-side is superdev mode. To enable it, uncomment devModeRedirectEnabled line from the end of DemoWidgetSet.gwt.xml located under lazylayouts-demo resources folder and compile the widgetset once by running vaadin:compile Maven target for lazylayouts-demo. Refresh lazylayouts-demo project resources by right clicking the project and choosing Refresh. Click "Create SuperDevMode Launch" button on the Vaadin tab of the lazylayouts-demo project properties panel to create superder mode code server launch configuration and modify the class path as instructed above. After starting the code server by running SuperDevMode launch as Java application, you can navigate to http://localhost:8080/lazylayouts-demo/?superdevmode. Now all code changes you do to your client side will get compiled as soon as you reload the web page. You can also access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings. 

 
## Release notes

### Version 0.2.0
- Moved to use and support Vaadin 8
- Addon uses scss theming now, make sure your project import and include addons scss

### Version 0.1.3
- Fixes server side warnings when LazyLayout has TabSheet as parent

### Version 0.1.2
- Improving demo application
- Now depending on Vaadin 7.3

### Version 0.1.1
- Fixes compiling issues with Vaadin 7.3

### Version 0.1.0
- Initial release, included LazyVerticalLayout
- Scrolling parent most be Panel or Window

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the
following features are planned for upcoming releases:
- Other layout types (LazyHorizontalLayout, HorizontalGridLayout?...)
- Support for other scrolling parents than Panel or Window

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

LazyLayouts is written by Sami Viitanen <sami.viitanen@vaadin.com>

# Developer Guide

## Getting started

Check the demo/test project included.

## Features

TODO

## API

LazyLayouts JavaDoc is available online at TODO