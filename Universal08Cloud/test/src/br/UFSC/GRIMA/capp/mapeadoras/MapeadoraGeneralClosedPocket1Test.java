package br.UFSC.GRIMA.capp.mapeadoras;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Point3d;

import org.junit.Before;
import org.junit.Test;

import br.UFSC.GRIMA.capp.movimentacoes.generatePath.GenerateContournParallel;
import br.UFSC.GRIMA.entidades.Material;
import br.UFSC.GRIMA.entidades.PropertyParameter;
import br.UFSC.GRIMA.entidades.features.Bloco;
import br.UFSC.GRIMA.entidades.features.Boss;
import br.UFSC.GRIMA.entidades.features.CircularBoss;
import br.UFSC.GRIMA.entidades.features.Face;
import br.UFSC.GRIMA.entidades.features.GeneralClosedPocket;
import br.UFSC.GRIMA.entidades.features.RectangularBoss;
import br.UFSC.GRIMA.util.DesenhadorDeLimitedElements;
import br.UFSC.GRIMA.util.Transformer;
import br.UFSC.GRIMA.util.Triangulation;
import br.UFSC.GRIMA.util.entidadesAdd.GeneralClosedPocketVertexAdd;
import br.UFSC.GRIMA.util.findPoints.LimitedElement;
import br.UFSC.GRIMA.util.geometricOperations.GeometricOperations;
import br.UFSC.GRIMA.util.projeto.Axis2Placement3D;
import br.UFSC.GRIMA.util.projeto.DadosDeProjeto;
import br.UFSC.GRIMA.util.projeto.Projeto;

public class MapeadoraGeneralClosedPocket1Test 
{
	private GeneralClosedPocket pocket = new GeneralClosedPocket();
	Projeto projeto;
	Bloco bloco;
	Face faceXY;

	@Before
	public void init()
	{
	    ArrayList<Point2D> points = new ArrayList<Point2D>();
	    //Forma 1
//		points.add(new Point2D.Double(500, 320));
//		points.add(new Point2D.Double(500, 160));
//		points.add(new Point2D.Double(280, 160));
//		points.add(new Point2D.Double(280, 40));
//		points.add(new Point2D.Double(0, 40));
//		points.add(new Point2D.Double(0, 320));
		
		//Forma 2
//		points.add(new Point2D.Double(500, 320));
//		points.add(new Point2D.Double(500, 0));
//		points.add(new Point2D.Double(0, 0));
//		points.add(new Point2D.Double(0, 320));
		
		//Forma de Cachorrinho --> raio 5
		points.add(new Point2D.Double(44.0,89.0));
		points.add(new Point2D.Double(51.0,68.0));
		points.add(new Point2D.Double(27.0,22.0));
		points.add(new Point2D.Double(55.0,20.0));
		points.add(new Point2D.Double(67.0,50.0));
		points.add(new Point2D.Double(124.0,65.0));
		points.add(new Point2D.Double(136.0,20.0));
		points.add(new Point2D.Double(164.0,19.0));
		points.add(new Point2D.Double(147.0,66.0));
		points.add(new Point2D.Double(168.0,116.0));
		points.add(new Point2D.Double(134.0,84.0));
		points.add(new Point2D.Double(68.0,84.0));
		points.add(new Point2D.Double(45.0,120.0));
		points.add(new Point2D.Double(13.0,93.0));
		
		//Forma 3
//		points.add(new Point2D.Double(30.0,64.0));
//		points.add(new Point2D.Double(75.0,28.0));
//		points.add(new Point2D.Double(160.0,78.0));
//		points.add(new Point2D.Double(141.0,125.0));
		
		//Protuberancia
		pocket.setPoints(points);
		pocket.setRadius(5);
		pocket.setPosicao(50, 50, 0);
		pocket.setProfundidade(15);
		ArrayList<Boss> itsBoss = new ArrayList<Boss>();
		CircularBoss arcBoss1 = new CircularBoss("", 200, 150, pocket.Z, 60, 60, pocket.getProfundidade());
		CircularBoss arcBoss2 = new CircularBoss("", 100, 200, pocket.Z, 40, 40, pocket.getProfundidade());
		CircularBoss arcBoss3 = new CircularBoss("", 36, 102, pocket.Z, 8, 8, pocket.getProfundidade());
		CircularBoss arcBossForma3 = new CircularBoss("", 97, 71, pocket.Z, 19, 19, pocket.getProfundidade());


//		itsBoss.add(arcBoss1);
//		itsBoss.add(arcBoss2);
		itsBoss.add(arcBoss3);
//		itsBoss.add(arcBossForma3);

		
		//Rectangular Boss
		RectangularBoss rectBoss = new RectangularBoss(40, 40, pocket.getProfundidade(), 0);
		rectBoss.setPosicao(200, 200, pocket.Z);
		rectBoss.setRadius(10);
//		itsBoss.add(rectBoss);
		pocket.setItsBoss(itsBoss);
		
		ArrayList<PropertyParameter> props = new ArrayList<PropertyParameter>();
		
		PropertyParameter properties = new PropertyParameter("Hardness","HB",250);
		
		props.add(properties);
		
		Material material = new Material("Aço", Material.ACO_ALTO_CARBONO, props);
		
		bloco = new Bloco(200.0, 150.0, 40.0, material);
		
		DadosDeProjeto dados = new DadosDeProjeto(123, "fulano da silva", "novo projeto", material);
		
		projeto = new Projeto(bloco, dados);
		
		new MapeadoraDeWorkingsteps(projeto);
//		new ToolManager(projeto);
		
		//adicionando feature na face
		faceXY = (Face)(bloco.faces.elementAt(Face.XY));
		faceXY.features.addElement(pocket);
		
		// ---- feature definition ----
		Point3d coordinates = new Point3d(pocket.X, pocket.Y, pocket.Z);
		ArrayList<Double> axis = new ArrayList<Double>();
		ArrayList<Double> refDirection = new ArrayList<Double>();
		axis.add(0.0);
		axis.add(0.0);
		axis.add(1.0);
		refDirection.add(1.0);
		refDirection.add(0.0);
		refDirection.add(0.0);
		Axis2Placement3D position = new Axis2Placement3D(coordinates, axis, refDirection);
		pocket.setPosition(position);
//		pocket.setPassante(true);
		pocket.setTolerancia(0.05);
		pocket.setRugosidade(0.05);
		
	}
	
	@Test
	public void getMenorMaiorDistanciaTest()
	{
		ArrayList<LimitedElement> all = new ArrayList<LimitedElement>();
		
		GeneralClosedPocketVertexAdd addPocketVertex = new GeneralClosedPocketVertexAdd(pocket.getPoints(), pocket.Z, pocket.getRadius());
		MapeadoraGeneralClosedPocket1 mp = new MapeadoraGeneralClosedPocket1(pocket);
//		double maiorMenorDistancia = mp.getMaiorMenorDistancia(pocket);
//		double menorMenorDistancia = mp.getMenorMenorDistance(pocket);
		double maiorMenorDistancia = mp.getMaiorMenorDistancia(GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z));
		double menorMenorDistancia = mp.getMenorMenorDistance(GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z));
//		Point3d point = new Point3d(100,100,0);
//		LimitedArc a1 = new LimitedArc(new Point3d(230,160,0),new Point3d(230,150,0),Math.PI/2);
//		LimitedArc a2 = new LimitedArc(new Point3d(470,30,0),new Point3d(470,0,0),Math.PI/2);
		for(LimitedElement elementTmp:addPocketVertex.getElements())
		{
			all.add(elementTmp);
		}
		for(ArrayList<LimitedElement> arrayTmp:GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z))
		{
			for(LimitedElement elementTmp:arrayTmp)
			{
				all.add(elementTmp);
			}
		}
//		System.out.println("Distance arc-arc: " + GeometricOperations.minimumDistance(a1, a2));
		System.out.println("Maior Menor Distancia: " + maiorMenorDistancia);
		System.out.println("Menor Menor distancia: " + menorMenorDistancia);
//		System.out.println("Propor��o (Menor / Maior): " + menorMenorDistancia/maiorMenorDistancia);
		DesenhadorDeLimitedElements desenhador = new DesenhadorDeLimitedElements(all);
		desenhador.setVisible(true);
		for(;;);
	}
	
	@Test
	public void getAlreadyDesbastedArea()
	{
		GeneralClosedPocketVertexAdd addPocket = new GeneralClosedPocketVertexAdd(pocket.getPoints(),pocket.Z, pocket.getRadius());
		Triangulation triangulation = new Triangulation(Transformer.limitedElementToPoints2D(addPocket.getElements()));
		System.err.println("Area Cavidade: " + triangulation.getArea());
		//Elementos da cavidade
		ArrayList<LimitedElement> formaOriginal = new ArrayList<LimitedElement>();
		//Todos os elementos (para printar)
		ArrayList<LimitedElement> all = new ArrayList<LimitedElement>();
		for(LimitedElement element:addPocket.getElements())
		{
			formaOriginal.add(element);
			//Comentar para nao ver os elementos da cavidade no desenhador
			all.add(element);
		}
		MapeadoraGeneralClosedPocket1 mp = new MapeadoraGeneralClosedPocket1(pocket);

		//PRIMEIRA FERRAMENTA
//		double diametroFerramenta1 = GeometricOperations.roundNumber(mp.getMaiorMenorDistancia(GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z)),2);
		double diametroFerramenta1 = 8;
		System.out.println("diametro ferramenta 1: " + diametroFerramenta1);
		double overLap = 2;//0.25*diametroFerramenta;
//		System.out.println("Menor menor distancia: " + mp.getMenorMenorDistance(GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z)));
//		GenerateContournParallel contourn = new GenerateContournParallel(pocket, pocket.Z, diametroFerramenta1, overLap);
//		for(ArrayList<ArrayList<LimitedElement>> matrixTmp:contourn.multipleParallelPath())
//		{
//			System.out.println("lol");
//			for(ArrayList<LimitedElement> arrayTmp:contourn.multipleParallelPath().get(0))
////			for(ArrayList<LimitedElement> arrayTmp:matrixTmp)
//			{
//				for(LimitedElement elementTmp:arrayTmp)
//				{
////					all.add(elementTmp);
//				}
//			}
//		}
		//WorkingStep 1 - GERACAO DO BOSS VIRTUAL (QUE INDICA O QUE JA FOI DESBASTADO)
		ArrayList<ArrayList<LimitedElement>> bossElements = MapeadoraGeneralClosedPocket1.getAreaAlreadyDesbastedTeste(pocket,null,pocket.Z, diametroFerramenta1, overLap);
		MapeadoraGeneralClosedPocket1.drawShape(formaOriginal, bossElements);
		triangulation = new Triangulation(Transformer.limitedElementToPoints2D(bossElements.get(0)));
		triangulation.drawTriangles();
		System.err.println("Area ferramenta 1: " + triangulation.getArea());
		for(ArrayList<LimitedElement> arrayTmp:GenerateContournParallel.gerarElementosDaProtuberancia(pocket, pocket.Z))
		{
//			bossElements.add(arrayTmp);
			for(LimitedElement elementTmp:arrayTmp)
			{
				//Comentar para nao ver o boss real no desenhador
				all.add(elementTmp);
			}
		}
		//SEGUNDA FERRAMENTA
		double diametroFerramenta2 = 5;
//		double diametroFerramenta2 = GeometricOperations.roundNumber(mp.getMaiorMenorDistancia(bossElements),2);
		System.out.println("diametro ferramenta 2: " + diametroFerramenta2);

		//Add os elementos das protuberancias (virtuais + reais) no array do desenhador
		for(ArrayList<LimitedElement> arrayTmp:bossElements)
		{
			for(LimitedElement elementTmp:arrayTmp)
			{
				formaOriginal.add(elementTmp);
				//Comentar para nao ver o bossvirtual da primeira ferramenta no desenhador
				all.add(elementTmp);
			}
		}
		
//		WorkingStep 2 - GERACAO DO BOSS VIRTUAL DA SEGUNDA FERRAMENTA
		GenerateContournParallel contourn = new GenerateContournParallel(pocket,bossElements, pocket.Z, diametroFerramenta2, overLap);
//		ELEMENTOS PARALELOS
//		for(ArrayList<LimitedElement> arrayTmp:contourn.parallelPath2Test(diametroFerramenta2))
//		{
//			for(LimitedElement elementTmp:arrayTmp)
//			{
//				all.add(elementTmp);
//			}
//		}
		//ELEMENTOS PARALELOS E QUEBRADOS
//		for(ArrayList<LimitedElement> arrayTmp:contourn.parallelPath2Test(diametroFerramenta2))
//		{
//			for(LimitedElement elementTmp:GenerateContournParallel.validar1Path(arrayTmp))
//			{
//				all.add(elementTmp);
//			}
//		}
//		ELEMENTOS PARALELOS, QUEBRADOS E VALIDADOS PELA MENOR DISTANCIA
//		for(ArrayList<LimitedElement> arrayTmp:contourn.parallelPath2Test(diametroFerramenta2))
//		{
//			for(LimitedElement elementTmp:GenerateContournParallel.validar2Path(GenerateContournParallel.validar1Path(arrayTmp),formaOriginal,diametroFerramenta2))
//			{
//				all.add(elementTmp);
//			}
//		}
		//Offset do que falta desbastar (apos utilizar 2 ferramentas)
//		for(ArrayList<ArrayList<LimitedElement>> matrixTmp:contourn.multipleParallelPath())
//		{
////			System.out.println("lol");
////			for(ArrayList<LimitedElement> arrayTmp:contourn.multipleParallelPath().get(0))
//			for(ArrayList<LimitedElement> arrayTmp:matrixTmp)
//			{
//				GeometricOperations.showElements(arrayTmp);
//				for(LimitedElement elementTmp:arrayTmp)
//				{
//					all.add(elementTmp);
//				}
//			}
//		}
		//Terceira ferramenta
		ArrayList<ArrayList<LimitedElement>> bossElements1 = MapeadoraGeneralClosedPocket1.getAreaAlreadyDesbastedTeste(pocket,bossElements,pocket.Z, diametroFerramenta2, overLap);
		MapeadoraGeneralClosedPocket1.drawShape(formaOriginal, bossElements1);
//		double diametroFerramenta3 = GeometricOperations.roundNumber(mp.getMaiorMenorDistancia(bossElements),2);
//		System.out.println("diametro ferramenta 3: " + diametroFerramenta3);
//		//Add os elementos das protuberancias (virtuais + reais) no array do desenhador
		for(ArrayList<LimitedElement> arrayTmp:bossElements1)
		{
			for(LimitedElement elementTmp:arrayTmp)
			{
//				formaOriginal.add(elementTmp);
				//Comentar para nao ver o bossvirtual da segunda ferramenta no desenhador
				all.add(elementTmp);
			}
		}
		DesenhadorDeLimitedElements desenhador = new DesenhadorDeLimitedElements(all);
		desenhador.setVisible(true);
		for(;;);
	}
	
	@Test
	public void getShapeTest()
	{
		Point2D point = new Point2D.Double(((CircularBoss)pocket.getItsBoss().get(0)).getCenter().x, ((CircularBoss)pocket.getItsBoss().get(0)).getCenter().y+50);
		ArrayList<ArrayList<LimitedElement>> bossElements = MapeadoraGeneralClosedPocket1.getAreaAlreadyDesbasted(pocket,null, pocket.Z, 75, 2);
		GeneralClosedPocketVertexAdd addPocket = new GeneralClosedPocketVertexAdd(pocket.getVertexPoints(), pocket.Z, pocket.getRadius());
//		final Shape gp = Face.getShape(pocket);
		final Shape gp = Face.getShape(addPocket.getElements());
		final ArrayList<Shape> bossShape = new ArrayList<Shape>();
		for(ArrayList<LimitedElement> bossTmp:bossElements)
		{
			bossShape.add(Face.getShape(bossTmp));
			System.out.println(Face.getShape(bossTmp).contains(point));
		}
		//Desenhador
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(750, 400));
		class Panel extends JPanel
		{
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
				
				g2d.translate(0, 400);
				g2d.scale(1, -1);
//				g2d.draw(gp);
				for(Shape shape:bossShape)
				{
//					g2d.setColor(new Color(155, 33, 12));
					g2d.fill(shape);
					g2d.setColor(new Color(15, 60, 212));
					g2d.draw(shape);
				}
//				for(ArrayList<Point2D> arrayTmp:matrix)
//				{
//					for(Point2D pointTmp:arrayPointTmp)
//					{
////						g2d.drawOval((int)pointTmp.getX(), (int)pointTmp.getY(), 1, 1);
//						g2d.draw(new Ellipse2D.Double(pointTmp.getX(), pointTmp.getY(), 1, 1));
//					}
//				}
			}
		}
		frame.getContentPane().add(new Panel());
		frame.setVisible(true);
		for(;;);
	}
	
}
