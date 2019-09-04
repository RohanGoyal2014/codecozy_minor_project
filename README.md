# Cloud Based Programming Contests Hosting Platform using Java Backend and Azure

<h2>Getting Started Steps</h2>
<ul>
<li>Create Maven Project without using Archetype and add maven dependencies and plugins in pom.xml<br>
  References: https://dzone.com/articles/how-to-create-a-web-project-using-maven-in-eclipse-1
  </li><li>Install Azure CLI and login, verify subsciption and add azure plugin after creating azure app service(jre8 and tomcat 8.5)<br>
  Reference: https://github.com/microsoft/azure-maven-plugins/blob/feature-spring/azure-webapp-maven-plugin/README.md</li>
<li>mvn clean install</li>
<li>mvn azure-webapp:deploy</li>
</ul>

<h2>Setting up azure sql database</h2>
<ul>
  <li>Create a sql database</li>
  <li>Get connection strings and use it to access the database</li>
</ul>

<h2>Custom Domain Pointing Steps:</h2>
<ul>
<li>Find custom domain settings for your app service</li>
<li>Add cname and a type record<br>
  References: https://www.youtube.com/watch?v=BVyesPHthoQ
  </li>
  <li> Click on add custom domain and type in your domain and select cname from dropdown and click on add custom domain
    </li>
  <li>Get SSL using cloudflare</li>
</ul>
