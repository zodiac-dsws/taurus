package br.cefetrj.sagitarii.core.types;

public enum ActivityType {
	MAP, SPLIT_MAP, REDUCE, SELECT, LOADER;
	
	public boolean isBlocking() {
		// Atividades que precisam aguardar mais de uma atividade ser concluída
		// Dependem de várias tabelas de entrada
		return ( this == ActivityType.REDUCE) || ( this == ActivityType.SELECT );
	}

	
	public boolean isJoin() {
		// Atividades que serão processadas pelo MainCluster
		return ( this == ActivityType.SELECT );
	}

}
