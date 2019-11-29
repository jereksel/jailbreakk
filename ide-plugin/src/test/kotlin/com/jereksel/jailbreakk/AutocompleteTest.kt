package com.jereksel.jailbreakk

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.pom.java.LanguageLevel
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import io.kotlintest.shouldBe
import org.jetbrains.kotlin.idea.core.completion.DeclarationLookupObject
import org.jetbrains.kotlin.name.FqName

class AutocompleteTest : LightJavaCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp()
//    val jar = classpathFromClassloader(this::class.java.classLoader)!!.single { it.absolutePath.contains("arrow-optics") }
//    PsiTestUtil.addLibrary(myFixture.getProjectDisposable(), myFixture.getModule(), "HibernateJPA", jar.absolutePath);
    }

    override fun getTestDataPath(): String {
        return "src/test/resources/testData";
    }

    override fun getProjectDescriptor(): LightProjectDescriptor {
        return object : ProjectDescriptor(LanguageLevel.HIGHEST) {

            override fun configureModule(module: Module, model: ModifiableRootModel, contentEntry: ContentEntry) {
                super.configureModule(module, model, contentEntry)
//                run {
//                    val jar = classpathFromClassloader(this::class.java.classLoader)!!.single { it.absolutePath.contains("arrow-annotations") }
//                    PsiTestUtil.addLibrary(model, "arrow-annotations", jar.parent, jar.name)
//                }
//                run {
//                    val jar = classpathFromClassloader(this::class.java.classLoader)!!.single { it.absolutePath.contains("arrow-optics") }
//                    PsiTestUtil.addLibrary(model, "arrow-optics", jar.parent, jar.name)
//                }
            }
        }
    }

    fun testJavaPrivateMethod() {
        myFixture.configureByFiles("SecretClass.java")
        myFixture.configureByFiles("SecretMain.kt")
        myFixture.complete(CompletionType.BASIC, 1)
        val strings = myFixture.lookupElementStrings ?: emptyList()
        strings shouldBe listOf("adder", "myField_field")
        ((myFixture.lookupElements!![0]).`object` as DeclarationLookupObject).importableFqName shouldBe FqName("jb.SecretClass.adder")
        ((myFixture.lookupElements!![1]).`object` as DeclarationLookupObject).importableFqName shouldBe FqName("jb.SecretClass.myField_field")
    }

    fun testKotlinPrivateMethod() {
        myFixture.configureByFiles("SecretClass.kt")
        myFixture.configureByFiles("SecretMain.kt")
        myFixture.complete(CompletionType.BASIC, 1)
        val strings = myFixture.lookupElementStrings ?: emptyList()
        strings shouldBe listOf("equals", "hashCode", "toString", "adder", "myField_field")
        ((myFixture.lookupElements!![3]).`object` as DeclarationLookupObject).importableFqName shouldBe FqName("jb.SecretClass.adder")
        ((myFixture.lookupElements!![4]).`object` as DeclarationLookupObject).importableFqName shouldBe FqName("jb.SecretClass.myField_field")
    }

}