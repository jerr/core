/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.javaee.cdi.ui;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.util.Arrays;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.javaee.ProjectHelper;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.test.UITestHarness;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.roaster.model.JavaAnnotation;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
@RunWith(Arquillian.class)
public class NewStereotypeCommandTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:ui"),
            @AddonDependency(name = "org.jboss.forge.addon:ui-test-harness"),
            @AddonDependency(name = "org.jboss.forge.addon:javaee"),
            @AddonDependency(name = "org.jboss.forge.addon:maven")
   })
   public static ForgeArchive getDeployment()
   {
      return ShrinkWrap
               .create(ForgeArchive.class)
               .addClass(ProjectHelper.class)
               .addBeansXML()
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:javaee"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:maven"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:ui"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:ui-test-harness")
               );
   }

   @Inject
   private UITestHarness testHarness;

   @Inject
   private ProjectHelper projectHelper;

   @Test
   public void testCreateNewStereotype() throws Exception
   {
      Project project = projectHelper.createJavaLibraryProject();
      CommandController controller = testHarness.createCommandController(NewStereotypeCommand.class,
               project.getRoot());
      controller.initialize();
      controller.setValueFor("named", "MyStereotype");
      controller.setValueFor("targetPackage", "org.jboss.forge.test");
      controller.setValueFor("targetTypes", Arrays.asList(ElementType.TYPE));
      Assert.assertTrue(controller.isValid());
      Assert.assertTrue(controller.canExecute());
      Result result = controller.execute();
      Assert.assertThat(result, is(not(instanceOf(Failed.class))));

      JavaSourceFacet facet = project.getFacet(JavaSourceFacet.class);
      JavaResource javaResource = facet.getJavaResource("org.jboss.forge.test.MyStereotype");
      Assert.assertNotNull(javaResource);
      Assert.assertThat(javaResource.getJavaType(), is(instanceOf(JavaAnnotation.class)));
      JavaAnnotation<?> ann = javaResource.getJavaType();
      Assert.assertTrue(ann.hasAnnotation(Stereotype.class));
      Assert.assertFalse(ann.hasAnnotation(Inherited.class));
      Assert.assertFalse(ann.hasAnnotation(Named.class));
      Assert.assertFalse(ann.hasAnnotation(Alternative.class));
   }

   @Test
   public void testCreateNewInheritedStereotype() throws Exception
   {
      Project project = projectHelper.createJavaLibraryProject();
      CommandController controller = testHarness.createCommandController(NewStereotypeCommand.class,
               project.getRoot());
      controller.initialize();
      controller.setValueFor("named", "MyStereotype");
      controller.setValueFor("targetPackage", "org.jboss.forge.test");
      controller.setValueFor("inherited", Boolean.TRUE);
      controller.setValueFor("targetTypes", Arrays.asList(ElementType.TYPE));
      Assert.assertTrue(controller.isValid());
      Assert.assertTrue(controller.canExecute());
      Result result = controller.execute();
      Assert.assertThat(result, is(not(instanceOf(Failed.class))));

      JavaSourceFacet facet = project.getFacet(JavaSourceFacet.class);
      JavaResource javaResource = facet.getJavaResource("org.jboss.forge.test.MyStereotype");
      Assert.assertNotNull(javaResource);
      Assert.assertThat(javaResource.getJavaType(), is(instanceOf(JavaAnnotation.class)));
      JavaAnnotation<?> ann = javaResource.getJavaType();
      Assert.assertTrue(ann.hasAnnotation(Stereotype.class));
      Assert.assertTrue(ann.hasAnnotation(Inherited.class));
      Assert.assertFalse(ann.hasAnnotation(Named.class));
      Assert.assertFalse(ann.hasAnnotation(Alternative.class));
   }

   @Test
   public void testCreateNewNamedStereotype() throws Exception
   {
      Project project = projectHelper.createJavaLibraryProject();
      CommandController controller = testHarness.createCommandController(NewStereotypeCommand.class,
               project.getRoot());
      controller.initialize();
      controller.setValueFor("named", "MyStereotype");
      controller.setValueFor("targetPackage", "org.jboss.forge.test");
      controller.setValueFor("withNamed", Boolean.TRUE);
      controller.setValueFor("targetTypes", Arrays.asList(ElementType.TYPE));
      Assert.assertTrue(controller.isValid());
      Assert.assertTrue(controller.canExecute());
      Result result = controller.execute();
      Assert.assertThat(result, is(not(instanceOf(Failed.class))));

      JavaSourceFacet facet = project.getFacet(JavaSourceFacet.class);
      JavaResource javaResource = facet.getJavaResource("org.jboss.forge.test.MyStereotype");
      Assert.assertNotNull(javaResource);
      Assert.assertThat(javaResource.getJavaType(), is(instanceOf(JavaAnnotation.class)));
      JavaAnnotation<?> ann = javaResource.getJavaType();
      Assert.assertTrue(ann.hasAnnotation(Stereotype.class));
      Assert.assertFalse(ann.hasAnnotation(Inherited.class));
      Assert.assertTrue(ann.hasAnnotation(Named.class));
      Assert.assertFalse(ann.hasAnnotation(Alternative.class));
   }

   @Test
   public void testCreateNewAlternativeStereotype() throws Exception
   {
      Project project = projectHelper.createJavaLibraryProject();
      CommandController controller = testHarness.createCommandController(NewStereotypeCommand.class,
               project.getRoot());
      controller.initialize();
      controller.setValueFor("named", "MyStereotype");
      controller.setValueFor("targetPackage", "org.jboss.forge.test");
      controller.setValueFor("alternative", Boolean.TRUE);
      controller.setValueFor("targetTypes", Arrays.asList(ElementType.TYPE));
      Assert.assertTrue(controller.isValid());
      Assert.assertTrue(controller.canExecute());
      Result result = controller.execute();
      Assert.assertThat(result, is(not(instanceOf(Failed.class))));

      JavaSourceFacet facet = project.getFacet(JavaSourceFacet.class);
      JavaResource javaResource = facet.getJavaResource("org.jboss.forge.test.MyStereotype");
      Assert.assertNotNull(javaResource);
      Assert.assertThat(javaResource.getJavaType(), is(instanceOf(JavaAnnotation.class)));
      JavaAnnotation<?> ann = javaResource.getJavaType();
      Assert.assertTrue(ann.hasAnnotation(Stereotype.class));
      Assert.assertFalse(ann.hasAnnotation(Inherited.class));
      Assert.assertFalse(ann.hasAnnotation(Named.class));
      Assert.assertTrue(ann.hasAnnotation(Alternative.class));
   }
}
