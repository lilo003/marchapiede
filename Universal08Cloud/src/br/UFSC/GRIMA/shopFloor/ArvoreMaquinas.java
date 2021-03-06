package br.UFSC.GRIMA.shopFloor;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import br.UFSC.GRIMA.entidades.ferramentas.BallEndMill;
import br.UFSC.GRIMA.entidades.ferramentas.BullnoseEndMill;
import br.UFSC.GRIMA.entidades.ferramentas.CenterDrill;
import br.UFSC.GRIMA.entidades.ferramentas.Ferramenta;
import br.UFSC.GRIMA.entidades.ferramentas.TwistDrill;
import br.UFSC.GRIMA.entidades.machiningResources.CuttingToolHandlingDevice;
import br.UFSC.GRIMA.entidades.machiningResources.MachineTool;
import br.UFSC.GRIMA.entidades.machiningResources.Spindle;
import br.UFSC.GRIMA.entidades.machiningResources.WorkpieceHandlingDevice;
import br.UFSC.GRIMA.util.projeto.Axis;
import br.UFSC.GRIMA.util.projeto.RotaryAxis;
import br.UFSC.GRIMA.util.projeto.TravelingAxis;

public class ArvoreMaquinas  
{
	private ProjetoSF projetoSF;
	private ArrayList<MachineTool> machines;
	
	public ArvoreMaquinas(ProjetoSF projetoSF)
	{
		this.projetoSF = projetoSF;
		this.machines = this.projetoSF.getShopFloor().getMachines();
	}
	public JTree getArvoreMaquinas()
	{
		JTree arvore = new JTree(this.criarRaizEFolhas());
		return arvore;
	}
	private DefaultMutableTreeNode criarRaizEFolhas()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Machines Tool List");
		for(int i = 0; i < this.machines.size(); i++)
		{
			MachineTool machineTmp = this.machines.get(i);
			DefaultMutableTreeNode root1 = new DefaultMutableTreeNode("Machine " + (i + 1));
			DefaultMutableTreeNode nodeGeneralTmp = new DefaultMutableTreeNode("General Data: ");
			nodeGeneralTmp.add(new DefaultMutableTreeNode("Id: " + machineTmp.getItsId()));
			nodeGeneralTmp.add(new DefaultMutableTreeNode("accuracy (um): " + machineTmp.getAccuracy()));
			nodeGeneralTmp.add(new DefaultMutableTreeNode("machine Location: " + machineTmp.getItsOrigin()));
			nodeGeneralTmp.add(new DefaultMutableTreeNode("Relative Cost ($/hour): " + machineTmp.getRelativeCost()));
			root1.add(nodeGeneralTmp);
			if(machineTmp.getToolHandlingDevice().get(0).getToolList().size() > 0)
			{
//				System.err.println(machineTmp.getToolHandlingDevice().size());
				root1.add(this.createNodeToolMagazine(machineTmp));
			}
			if(machineTmp.getWorkpieceHandlingDevice().size() > 0)
			{
				root1.add(this.createNodeWorkpieceHandlingDevices(machineTmp));
			}
			if(machineTmp.getItsSpindle().size() > 0)
			{
				root1.add(this.createNodeItsSpindle(machineTmp));
			}
			if(machineTmp.getAxis().size() > 0)
			{
				root1.add(this.createAxis(machineTmp));
			}
			root.add(root1);
		}
		return root;
	}
	private DefaultMutableTreeNode createAxis(MachineTool machine)
	{
		ArrayList<Axis> axis = machine.getAxis();
		DefaultMutableTreeNode axisNode = new DefaultMutableTreeNode("Axis");
		
		for(int i = 0; i < axis.size(); i++)
		{
			Axis axisTmp = axis.get(i);
			if(axisTmp.getClass() == TravelingAxis.class)
			{
				TravelingAxis travel = (TravelingAxis)axisTmp;
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("Axis " + (i + 1) +" -> " + axisTmp.getTypeOfAxisString());
				node.add(new DefaultMutableTreeNode("Traveling Range (mm): "+  travel.getName()));
				node.add(new DefaultMutableTreeNode("Max Feed Rate Speed (mm/min): "+  travel.getItsFeedRateRange()));
				node.add(new DefaultMutableTreeNode("Traveling Range (mm): "+  travel.getItsTravelingRange()));
				node.add(new DefaultMutableTreeNode("Rapid Movement Speed (m/min): " + travel.getRapidMovementSpeed()));
				node.add(new DefaultMutableTreeNode("Its Origin: " + travel.getOrigin()));
				axisNode.add(node);
			} else if(axisTmp.getClass() == RotaryAxis.class)
			{
				RotaryAxis rotary = (RotaryAxis)axisTmp;
				DefaultMutableTreeNode node = new DefaultMutableTreeNode("Axis " + (i + 1) +" -> " + axisTmp.getTypeOfAxisString());
				node.add(new DefaultMutableTreeNode("Traveling Range (mm): "+  rotary.getName()));
				node.add(new DefaultMutableTreeNode("Max Root Speed (rpm): "+  rotary.getMaxRotSpeed()));
				node.add(new DefaultMutableTreeNode("Its Origin: " + rotary.getOrigin()));
				axisNode.add(node);
			}
		}
		return axisNode;
	}
	private DefaultMutableTreeNode createNodeWorkpieceHandlingDevices(MachineTool machine)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Its Handling Devices: ");
		for(int i = 0; i < machine.getWorkpieceHandlingDevice().size(); i++)
		{
			WorkpieceHandlingDevice workpieceHandlingDeviceTmp = machine.getWorkpieceHandlingDevice().get(i);
			DefaultMutableTreeNode nodeTmp = new DefaultMutableTreeNode("" + workpieceHandlingDeviceTmp.getItsId());
			nodeTmp.add(new DefaultMutableTreeNode("Max load capacity: " + workpieceHandlingDeviceTmp.getMaxLoadCapacity()));
			nodeTmp.add(new DefaultMutableTreeNode("type: " + workpieceHandlingDeviceTmp.getType()));
			node.add(nodeTmp);
		}
		return node;
	}
	private DefaultMutableTreeNode createNodeToolMagazine(MachineTool machine) 
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Its ToolMagazine: ");
		CuttingToolHandlingDevice magazine = machine.getToolHandlingDevice().get(0);
		node.add(new DefaultMutableTreeNode("Its Tool Capacity: " + magazine.getItsToolCapacity()));
		node.add(new DefaultMutableTreeNode("Its Max allowed tool weight: " + magazine.getMaxAllowedToolWeight()));
		node.add(new DefaultMutableTreeNode("Its Max allowed tool length: " + magazine.getMaxAllowedToolLength()));
		node.add(new DefaultMutableTreeNode("Its Current Tool : " + magazine.getItsCurrentTool()));
		// por ora so esses aew
		DefaultMutableTreeNode listaDeFerramentas = new DefaultMutableTreeNode("Tool List in Magazine");

		for(int i = 0; i < magazine.getToolList().size(); i++)
		{
			Ferramenta tool = magazine.getToolList().get(i);
			DefaultMutableTreeNode nodoFerramentaTmp = new DefaultMutableTreeNode("Tool " + (i + 1) + " -> "+ tool.getName());
		
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Type : " + tool.getClass().toString().substring(42)));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Diameter : " + tool.getDiametroFerramenta() + " mm"));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Cutting Edge Length : " + tool.getCuttingEdgeLength() + " mm"));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Max Depth : " + tool.getProfundidadeMaxima() + " mm"));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Off Set Length : " + tool.getOffsetLength() + " mm"));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Hand Of Cut : " + tool.getStringHandOfCut()));
			nodoFerramentaTmp.add(new DefaultMutableTreeNode("Material : Carbide - " + tool.getMaterial()));
			if(tool.getClass() == CenterDrill.class || tool.getClass() == TwistDrill.class)
			{
				nodoFerramentaTmp.add(new DefaultMutableTreeNode("Tip Tool Half Angle : " + tool.getToolTipHalfAngle() + " °"));
			} else if (tool.getClass() == BallEndMill.class)
			{
				nodoFerramentaTmp.add(new DefaultMutableTreeNode("Edge Radius: " + tool.getEdgeRadius() + " mm"));
			} else if (tool.getClass() == BullnoseEndMill.class)
			{
				nodoFerramentaTmp.add(new DefaultMutableTreeNode( "Edge Radius: " + tool.getEdgeRadius() + " mm"));
				nodoFerramentaTmp.add(new DefaultMutableTreeNode("Edge Center Vertical: " + tool.getEdgeCenterVertical() + " mm"));
				nodoFerramentaTmp.add(new DefaultMutableTreeNode("Edge Center Horizontal: " + tool.getEdgeCenterHorizontal() + " mm"));				
			}		
			listaDeFerramentas.add(nodoFerramentaTmp);
		}
		node.add(listaDeFerramentas);
		 return node;
	}
	private DefaultMutableTreeNode createNodeItsSpindle(MachineTool machine)
	{
		DefaultMutableTreeNode nodeItsSpindle = new DefaultMutableTreeNode("Its Spindles:");
		Spindle spindle;
		for(int i = 0; i < machine.getItsSpindle().size(); i++)
		{
			spindle = machine.getItsSpindle().get(i);
			nodeItsSpindle.add(new DefaultMutableTreeNode("Its Id: " + spindle.getItsId()));
			nodeItsSpindle.add(new DefaultMutableTreeNode("Max Power (kW): " + spindle.getSpindleMaxPower()));
			nodeItsSpindle.add(new DefaultMutableTreeNode("Max Speed (rpm): " + spindle.getItsSpeedRange()));
			nodeItsSpindle.add(new DefaultMutableTreeNode("Max Torque (N-m): " + spindle.getMaxTorque()));
		}
		return nodeItsSpindle;
	}
}
