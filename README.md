# EMF Façade

A model façade implementation for the Eclipse Modeling Framework (EMF).

Includes support for synchronization of façade models with instances of
DSMLs based on *UML Profiles*.

## Source Tree Layout

The project is structured in four folders:

* `plugins/` contains all Eclipse plug-ins (OSGi bundles) that comprise the EMF Façade
framework
* `features/` contains the Eclipse features that install the plug-in bundles
* `tests/` collects the test bundles
* `releng/` contains modules that implement the build system and development environment, such as build and publish scripts, the p2 repository assembly, target definitions, and
the Oomph setup model 

## Workspace Set-up

To set up a complete development environment, it should be sufficient to add the [EMFFacade.setup](https://raw.githubusercontent.com/cdamus/emf.facade/master/releng/EMFFacade.setup)
file to your [Eclipse Installer](https://www.eclipse.org/downloads/) and import the
**EMF Facade** project from the **GitHub Projects** category.  The best results will be
obtained with an installation based on the Oxygen.2 or later version of the
*Eclipse Modeling Tools* package product.

## Testing

Automated test bundles are be located in the `tests` folder.

## Building

Run the following command from the repository root:

```shell
    $ mvn -Poxygen clean verify
```

to build and test everything (without installing) on the Eclipse Oxygen plaform.  There
is also a `photon` profile for the Eclipse Photon platform that is currently in
development.
