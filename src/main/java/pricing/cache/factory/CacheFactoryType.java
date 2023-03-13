package pricing.cache.factory;

public enum CacheFactoryType {
		METADATA_CACHE("buying-promotion");

		private String key = null;

		private CacheFactoryType(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return this.key;
		}
}
