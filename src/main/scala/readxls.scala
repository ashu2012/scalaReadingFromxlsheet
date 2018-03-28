package brd
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook

import scala.App
import scala.collection.mutable.HashMap

import java.time
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
object  readxls {
  def main(args: Array[String]) {
    println("reading from xlsheet")
    var excelFilePath = "book.xlsx"
    var inputStream = new FileInputStream(new File(excelFilePath));
    var workbook = new XSSFWorkbook(inputStream);
    //in my case the
    var firstSheet = workbook.getSheetAt(3);
    var iterator = firstSheet.iterator();

    var caseSrc=""
    var caseTarget=""
    /*
    while (iterator.hasNext()) {
      var nextRow = iterator.next();
      var cellIterator = nextRow.cellIterator();

      while (cellIterator.hasNext()) {
        var cell = cellIterator.next();
        //System.out.print(cell.getStringCellValue());
        cell.getCellType() match {
          case Cell.CELL_TYPE_STRING =>
            System.out.print(cell.getStringCellValue());
          case Cell.CELL_TYPE_BOOLEAN =>
            System.out.print(cell.getBooleanCellValue());
          case Cell.CELL_TYPE_NUMERIC =>
            System.out.print(cell.getNumericCellValue());
          case Cell.CELL_TYPE_BLANK =>
            print("-----Blank Value-----")
        }

      }
      println("Row ends --------------------------------------------------- ");
      System.out.println();
    }
*/

    var sourceCaseClassObj = new buildCaseClass()
    var targetCaseClassObj =  new buildCaseClass()
    var transformationRules =  new buildTrasnformationTrait()

    sourceCaseClassObj.initiateCaseClass("SourceCaseClass")
    targetCaseClassObj.initiateCaseClass("targetCaseClass")
    transformationRules.initiateTraitStr()

    var headerRemoved:Boolean = false

    while (iterator.hasNext()) {
      var nextRow = iterator.next();
      var cellIterator = nextRow.cellIterator();

      var rowdata = scala.collection.mutable.ArrayBuffer[Any]()

      var consInputStr = ""

      while (cellIterator.hasNext()) {
        var cell = cellIterator.next();
        //System.out.print(cell.getStringCellValue());
        cell.getCellType() match {
          case Cell.CELL_TYPE_STRING =>
            //System.out.print(cell.getStringCellValue());
            rowdata.append(cell.getStringCellValue())
          case Cell.CELL_TYPE_BOOLEAN =>
            // System.out.print(cell.getBooleanCellValue());
            rowdata.append(cell.getBooleanCellValue())
          case Cell.CELL_TYPE_NUMERIC =>
            // System.out.print(cell.getNumericCellValue());
            rowdata.append(cell.getNumericCellValue())
          case Cell.CELL_TYPE_BLANK =>
          // print("-----Blank Value-----")
           rowdata.append("")
        }

      }

      if (headerRemoved == true) {

        sourceCaseClassObj.updateCaseClassSourceClouser(
          rowdata(4).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", ""),
          rowdata(2).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", "")
        )

        targetCaseClassObj.updateCaseClassSourceClouser(
          rowdata(3).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", ""),
          rowdata(2).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", "")
        )

        transformationRules.updateTraitStrClouser(
          rowdata(4).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", ""),
          rowdata(3).toString.trim.replaceAll("(\n)*(\r)*", "").replaceAll("\\s", ""),
          rowdata(2).toString.trim.replaceAll("(\n)*(\r)*", ""),
          rowdata(6).toString
        )

      }

      headerRemoved=true
      println("Row ends --------------------------------------------------- ");

    }
    workbook.close()
    inputStream.close()
    sourceCaseClassObj.endCaseClass()
    targetCaseClassObj.endCaseClass()
    transformationRules.endBuilding()

    //println(sourceCaseClassObj.initialConsSourceStr)
    //println(targetCaseClassObj.initialConsSourceStr)
    //println(transformationRules.initialTraitStr)

    val lst = scala.collection.mutable.ListBuffer[String](sourceCaseClassObj.initialConsSourceStr,targetCaseClassObj.initialConsSourceStr,transformationRules.initialTraitStr )

    Files.write(Paths.get("output.txt"),  lst.mkString("\n").getBytes(StandardCharsets.UTF_8))


  }


  //case class target string
  var initialConsTargetstr: String = initialConsTargetstr match {
    case null => ""
    case _ => initialConsTargetstr
  }


  def createCaseClassTargetClouser(inputTarget: String, inputType: String): String = {
    var clouser = (inputTarget: String, inputType: String) => initialConsTargetstr + " val " + inputTarget + " : " + inputType + " ,"
    initialConsTargetstr = clouser(inputTarget, inputType)
    initialConsTargetstr
  }



}


trait  typeInformation{


  def createScalaType(typeStr: String):String = {



    def matchTIMESTAMP(input: String) = {
      val regStr = raw"(.*TIMESTAMP.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }


    def matchVARCHAR(input: String) = {
      val regStr = raw"(.*CHAR.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }

    def matchINT(input: String) = {
      val regStr = raw"(.*INT.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }


    def matchDATE(input: String) = {
      val regStr = raw"(.*DATE.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }


    def matchBOOLEAN(input: String) = {
      val regStr = raw"(.*BOOLEAN.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }


    def matchDOUBLE(input: String) = {
      val regStr = raw"(.*DECIMAL.*)".r
      input match {
        case regStr(_*) => true
        case _ => false
      }
    }


    typeStr match {
      case typeStr if matchTIMESTAMP(typeStr) == true =>  "TIMMESTAMP"
      case typeStr if matchVARCHAR(typeStr) == true => "String"
      case typeStr if matchINT(typeStr) == true => "Int"
      case typeStr if matchDATE(typeStr) == true => "Date"
      case typeStr if matchBOOLEAN(typeStr) == true => "Boolean"
      case typeStr if matchDOUBLE(typeStr) == true => "Double"

    }

  }

}

class  buildCaseClass() extends typeInformation{



  //case class source string
  var initialConsSourceStr: String = initialConsSourceStr match {
    case null => ""
    case _ => initialConsSourceStr
  }

  val hm=  scala.collection.mutable.HashMap[String, String]()


  def initiateCaseClass(name:String)={
    initialConsSourceStr= initialConsSourceStr + " case class "+ name + " ("

  }
  def updateCaseClassSourceClouser(inputSrc: String, inputType: String): String = {

    hm.get(inputSrc)  match {
      case x: Some[String] => {
        println("key already exist! don't create case term")
        initialConsSourceStr
      }
      case None=> {
        println("key not exist")
        hm.put(inputSrc, inputSrc)
        var clouser = (inputSrc: String, inputType: String) => initialConsSourceStr + "  " + inputSrc + " : " + createScalaType(inputType) +" ,"
        initialConsSourceStr = clouser(inputSrc, inputType)
        initialConsSourceStr
      }
    }

  }


  def endCaseClass()={
    initialConsSourceStr= initialConsSourceStr.dropRight(1) + " ) "
  }



}

class buildTrasnformationTrait() extends typeInformation {



  //Trasformation trait string for clouser
  var initialTraitStr: String = initialTraitStr match {
    case null => ""
    case _ => initialTraitStr
  }


  def initiateTraitStr()={
    initialTraitStr= initialTraitStr + " trait transformationRules  {\n"
  }

  def updateTraitStrClouser(inputSrc: String, inputTarget: String, inputType: String, inputRule: String): String = {
    var clouser = (inputSrc: String, inputTarget: String, inputType: String, inputRule: String) => {
      initialTraitStr = initialTraitStr + " \n" +
        s"""
           |def ${inputSrc}To${inputTarget}(   ):${createScalaType(inputType)} ={
           |\"\"\"
           |Source Column Name: ${inputSrc}
           |Target Column Name: ${inputTarget}
           |Transformation Rule: ${inputRule}

           |\"\"\"
           |
           |
           |}
           """.stripMargin

      initialTraitStr
    }
    initialTraitStr = clouser(inputSrc , inputTarget,  inputType  , inputRule)
    initialTraitStr
  }


  def endBuilding()={
    initialTraitStr= initialTraitStr.dropRight(1) + " } "
  }
}