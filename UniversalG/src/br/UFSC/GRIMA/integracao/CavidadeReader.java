package br.UFSC.GRIMA.integracao;

import java.util.ArrayList;

import javax.vecmath.Point3d;

import jsdai.SMachining_schema.EBlock;
import jsdai.SMachining_schema.EClosed_pocket;
import jsdai.SMachining_schema.EPlus_minus_value;
import jsdai.SMachining_schema.ERectangular_closed_profile;
import jsdai.SMachining_schema.EThrough_pocket_bottom_condition;
import jsdai.lang.SdaiException;
import br.UFSC.GRIMA.entidades.features.Cavidade;
import br.UFSC.GRIMA.entidades.features.Face;
import br.UFSC.GRIMA.util.projeto.Axis2Placement3D;

public class CavidadeReader {

	public static Cavidade getCavidade(EClosed_pocket pocket)
			throws SdaiException {

		String id = pocket.getIts_id(null);
		
		double locX = pocket.getFeature_placement(null).getLocation(null)
				.getCoordinates(null).getByIndex(1);
		double locY = pocket.getFeature_placement(null).getLocation(null)
				.getCoordinates(null).getByIndex(2);		
		double locZ = pocket.getFeature_placement(null).getLocation(null)
				.getCoordinates(null).getByIndex(3);
		
		
		double raioCavidade = pocket.getOrthogonal_radius(null).getTheoretical_size(null);

		double largura = ((ERectangular_closed_profile) pocket
				.getFeature_boundary(null)).getProfile_width(null)
				.getTheoretical_size(null);

		double comprimento = ((ERectangular_closed_profile) pocket
				.getFeature_boundary(null)).getProfile_length(null)
				.getTheoretical_size(null);

		double profundidadeCavidade = pocket.getDepth(null).getPosition(null)
				.getLocation(null).getCoordinates(null).getByIndex(3)
				*(-1);
		
		double alturaBloco = ((EBlock)pocket.getIts_workpiece(null).getIts_bounding_geometry(null)).getZ(null);

//		double x = locX - comprimentoCavidade/2;
//		double y = locY - larguraCavidade/2;
//		double z = alturaBloco - locZ;
		
		double x = 0, y = 0, z = 0;
		
		Face faceAtual = FaceReader.getFace(pocket);
		
		if(faceAtual.getTipo()==Face.XY){

			x = locX - comprimento/2;

			y = locY - largura/2;
		
			z = faceAtual.getProfundidadeMaxima() - locZ;

		}else if(faceAtual.getTipo()==Face.YX){

			x = locX - comprimento/2;

			y = faceAtual.getLargura() - locY - largura/2;

			z = locZ;

		}else if(faceAtual.getTipo()==Face.YZ){

			x = faceAtual.getComprimento() - comprimento/2 - locZ;

			y = locY - largura/2;

			z = faceAtual.getProfundidadeMaxima() - locX;

		}else if(faceAtual.getTipo()==Face.ZY){

			x = locZ - comprimento/2;

			y = locY - largura/2;

			z = locX;

		}else if(faceAtual.getTipo()==Face.XZ){

			x = locX - comprimento/2;

			y = locZ - largura/2;

			z = locY;

		}else if(faceAtual.getTipo()==Face.ZX){

			x = locX - comprimento/2;

			y = faceAtual.getLargura()- locZ - largura/2;

			z = faceAtual.getProfundidadeMaxima() - locY;

		}

		Cavidade cavidade = new Cavidade(id, x, y, z, locX, locY, locZ, raioCavidade, largura, comprimento, profundidadeCavidade);

		double tolerancia = ((EPlus_minus_value)pocket.getOrthogonal_radius(null).getImplicit_tolerance(null)).getUpper_limit(null);
		cavidade.setTolerancia(tolerancia);
		
		boolean b;
		
		if (pocket.getBottom_condition(null).isKindOf(EThrough_pocket_bottom_condition.class))
			b = true;
		else b = false;
		
		cavidade.setPassante(b);
		
		double zLinhaX = pocket.getFeature_placement(null).getAxis(null).getDirection_ratios(null).getByIndex(1);
		double zLinhaY = pocket.getFeature_placement(null).getAxis(null).getDirection_ratios(null).getByIndex(2);
		double zLinhaZ = pocket.getFeature_placement(null).getAxis(null).getDirection_ratios(null).getByIndex(3);
		
		
		ArrayList<Double> axis = new ArrayList<Double>();
		axis.add(zLinhaX);
		axis.add(zLinhaY);
		axis.add(zLinhaZ);
		
		zLinhaX = pocket.getFeature_placement(null).getRef_direction(null).getDirection_ratios(null).getByIndex(1);
		zLinhaY = pocket.getFeature_placement(null).getRef_direction(null).getDirection_ratios(null).getByIndex(2);
		zLinhaZ = pocket.getFeature_placement(null).getRef_direction(null).getDirection_ratios(null).getByIndex(3);
		
		
		ArrayList<Double> refDirection = new ArrayList<Double>();
		refDirection.add(zLinhaX);
		refDirection.add(zLinhaY);
		refDirection.add(zLinhaZ);
		
		double zOriginal = pocket.getFeature_placement(null).getLocation(null)
		.getCoordinates(null).getByIndex(3);
		
		Point3d coordinates = new Point3d(x,y,zOriginal);
		
		Axis2Placement3D position = new Axis2Placement3D(coordinates,axis,refDirection);
		
		cavidade.setPosition(position);
		
		return cavidade;
	}

}
