package io.haibiiin.github.query.engine.cache

class LuaScripts {

    static String leaseSet() {
        return """
            local key = KEYS[1]
            local token = ARGV[1]
            local value = ARGV[2]
            local lease_key = 'lease:'..key
            local lease_value = redis.call('get', lease_key)
            if lease_value == token then
                redis.replicate_commands()
                redis.call('set', key, value)
                return {value, true}
            else
                return {false, false}
            end
        """
    }

    static String leaseGet() {
        return """
            local key = KEYS[1]
            local token = ARGV[1]
            local value = redis.call('get', key)
            if not value then
                redis.replicate_commands()
                local lease_key = 'lease:'..key
                redis.call('set', lease_key, token)
                return {false, false}
            else
                return {value, true}
            end
        """
    }
}